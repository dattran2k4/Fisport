package com.fisport.service.impl;

import com.fisport.common.*;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.*;
import com.fisport.repository.*;
import com.fisport.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.model.webhooks.WebhookData;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class WalletPaymentServiceImpl implements WalletPaymentService {

    private final VnPayService vnPayService;
    private final WalletService walletService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BookingRepository bookingRepository;
    private final ChallengeMatchRepository challengeMatchRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;

    @Transactional
    @Override
    public void handleVnPayReturn(Map<String, String> params) {

        boolean valid = vnPayService.validatePayment(params);
        if (!valid) {
            throw new InvalidDataException("Invalid VNPay callback");
        }

        Long paymentId = vnPayService.extractId(params);
        Payment payment = paymentService.getPaymentById(paymentId);

        Transaction transaction = transactionRepository.findByPayment(payment).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        User user = transaction.getWallet().getUser();
        log.info("VNPay callback for user: {} (ID: {})", user.getUsername(), user.getId());

        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        String response = params.get("vnp_ResponseCode");
        String transactionNo = Optional.ofNullable(params.get("vnp_TransactionNo")).orElse("UNKNOWN");

        LocalDateTime paymentTime = null;
        switch (response) {
            case "00":
                payment.setStatus(EPaymentStatus.SUCCESS);
                paymentTime = LocalDateTime.now();
                transaction.setStatus(ETransactionStatus.SUCCESS);
                walletService.creditWallet(transaction);
                break;
            case "24":
                payment.setStatus(EPaymentStatus.CANCELLED);
                transaction.setStatus(ETransactionStatus.FAILED);
                break;
            default:
                payment.setStatus(EPaymentStatus.FAILED);
                transaction.setStatus(ETransactionStatus.FAILED);
                break;
        }

        payment.setPaymentTime(paymentTime);
        payment.setTransactionCode(transactionNo);
        paymentRepository.save(payment);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void handlePayOSReturn(WebhookData data) {
        Long paymentId = data.getOrderCode() % 100000;
        log.info("handlePayOSReturn called, paymentId = {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy payment"));

        Transaction transaction = transactionRepository.findByPayment(payment).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy transaction"));
        log.info("handlePayOSReturn called, transactionId = {}", transaction.getId());

        if (data.getCode().equals("00") && data.getDesc().equals("success")) {
            transaction.setStatus(ETransactionStatus.SUCCESS);
            payment.setStatus(EPaymentStatus.SUCCESS);
            payment.setTransactionCode(data.getReference());
            payment.setPaymentTime(LocalDateTime.now());
            walletService.creditWallet(transaction);
        } else {
            payment.setStatus(EPaymentStatus.FAILED);
            transaction.setStatus(ETransactionStatus.FAILED);
        }

        paymentRepository.save(payment);
        log.info("Payment update status: " + payment.getStatus());

        transactionRepository.save(transaction);
        log.info("Transaction update status: " + transaction.getStatus());
    }

    @Transactional
    @Override
    public void payBooking(String paymentToken, String username) {
        Booking booking = paymentService.findByPaymentToken(paymentToken);

        if (!booking.getBookingStatus().equals(EBookingStatus.PENDING)) {
            throw new InvalidDataException("Payment paid already");
        }

        booking.setPaymentMethod(EPaymentMethod.WALLET);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wallet wallet = walletRepository.findByUser(user);
        log.info("Wallet id: " + wallet.getId());

        Transaction transaction = Transaction.builder()
                .type(ETransactionType.PAYMENT)
                .amount(booking.getTotalPrice())
                .status(ETransactionStatus.PENDING)
                .method(EPaymentMethod.WALLET)
                .booking(booking)
                .wallet(wallet)
                .build();

        transactionRepository.save(transaction);

        if (wallet.getBalance().compareTo(transaction.getAmount()) < 0) {
            transaction.setStatus(ETransactionStatus.FAILED);
            transactionRepository.save(transaction);
            log.warn("Balance of userId {} not enough for bookingId ", username, booking.getId());
            throw new InvalidDataException("Not enough balance");
        }

        walletService.debitWallet(wallet.getId(), transaction);

        transaction.setStatus(ETransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
        log.info("Transaction update status: " + transaction.getStatus());

        booking.setBookingStatus(EBookingStatus.PAID);
        bookingRepository.save(booking);
        log.info("Booking {} paid", booking.getId());

        ChallengeMatch match = challengeMatchRepository.findByBooking(booking).orElse(null);

        if (match != null) {
            match.setStatus(EChallengeStatus.OPEN);
            challengeMatchRepository.save(match);
            log.info("Challenge match {} update status: ", match.getId(), match.getStatus());
        }

        log.info("wallet paid successfully: userId={}, bookingId={}", username, booking.getId());
    }

    @Transactional
    @Override
    public void refundBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));

        User user = booking.getUser();

        Wallet wallet = walletRepository.findByUser(user);

        if (!booking.getBookingStatus().equals(EBookingStatus.PAID)) {
            throw new InvalidDataException("Invalid booking status");
        }

        Transaction transaction = Transaction.builder()
                .type(ETransactionType.REFUND)
                .amount(booking.getTotalPrice())
                .status(ETransactionStatus.PENDING)
                .booking(booking)
                .wallet(wallet)
                .build();

        transactionRepository.save(transaction);

        try {
            wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
            walletRepository.save(wallet);

            transaction.setStatus(ETransactionStatus.SUCCESS);
            transactionRepository.save(transaction);

            booking.setBookingStatus(EBookingStatus.CANCELLED);
            bookingRepository.save(booking);

            log.info("Refund success for booking {}: amount {}", bookingId, transaction.getAmount());
        } catch (Exception e) {
            transaction.setStatus(ETransactionStatus.FAILED);
            transactionRepository.save(transaction);
            log.error("Refund failed for booking {}: {}", bookingId, e.getMessage());
            throw new InvalidDataException("Refund failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void payChallengeMatch(Long matchId, String username) {
        ChallengeMatch match = challengeMatchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        User player = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ChallengeParticipant pariticipant = challengeParticipantRepository.findByUserIdAndMatchId(player.getId(), matchId).orElseThrow(() -> new ResourceNotFoundException("Không có sự tham gia nào ở đây"));

        if (!pariticipant.getStatus().equals(EParticipantStatus.ACCEPTED)) {
            throw new InvalidDataException("Bạn chưa được chấp nhận tham gia trận đấu");
        }

        if (pariticipant.isPaid()) {
            throw new InvalidDataException("Bạn đã trả tiền rồi");
        }

        if (player.getWallet().getBalance().compareTo(match.getParticipationFee()) < 0) {
            throw new InvalidDataException("Số dư không đủ");
        }

        Transaction transactionPlayer = Transaction.builder()
                .status(ETransactionStatus.SUCCESS)
                .method(EPaymentMethod.WALLET)
                .amount(match.getParticipationFee())
                .type(ETransactionType.PAYMENT)
                .wallet(player.getWallet())
                .build();

        transactionRepository.save(transactionPlayer);

        walletService.debitWallet(player.getWallet().getId(), transactionPlayer);
        log.info("playerId {} paid {}", player.getId(), transactionPlayer.getAmount());

        Transaction transactionCreator = Transaction.builder()
                .amount(transactionPlayer.getAmount())
                .wallet(match.getCreator().getWallet())
                .type(ETransactionType.RECEIVED)
                .method(EPaymentMethod.WALLET)
                .status(ETransactionStatus.SUCCESS)
                .build();

        transactionRepository.save(transactionCreator);

        walletService.creditWallet(transactionCreator);
        log.info("creatorId {} recevied {}", match.getCreator().getId(), transactionCreator.getAmount());

        pariticipant.setPaid(true);
        pariticipant.setPaidAt(LocalDateTime.now());

        challengeParticipantRepository.save(pariticipant);

        log.info("PlayerId {} pay success for MatchId {}", player.getId(), matchId);
    }
}
