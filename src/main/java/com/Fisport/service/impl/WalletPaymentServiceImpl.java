package com.Fisport.service.impl;

import com.Fisport.common.*;
import com.Fisport.model.*;
import com.Fisport.repository.*;
import com.Fisport.service.*;
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

    private final VnPayService  vnPayService;
    private final WalletService walletService;
    private final PaymentService paymentService;
    private final PaymentRepository  paymentRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public void handleVnPayReturn(Map<String, String> params) {

        boolean valid = vnPayService.validatePayment(params);
        if (!valid) {
            throw new RuntimeException("Invalid VNPay callback");
        }

        Long paymentId = vnPayService.extractId(params);
        Payment payment = paymentService.getPaymentById(paymentId);

        Transaction transaction =  transactionRepository.findByPayment(payment);
        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        User user = transaction.getWallet().getUser();
        log.info("VNPay callback for user: {} (ID: {})", user.getUsername(), user.getId());

        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
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

        Payment payment = paymentService.getPaymentById(paymentId);

        Transaction transaction =  transactionRepository.findByPayment(payment);

        if (data.getCode().equals("00") && data.getDesc().equals("success")) {
            transaction.setStatus(ETransactionStatus.SUCCESS);
            payment.setStatus(EPaymentStatus.SUCCESS);
            payment.setTransactionCode(data.getReference());
            payment.setPaymentTime(LocalDateTime.now());
            transactionRepository.save(transaction);
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
        log.info("Booking: " + booking);

        if (!booking.getBookingStatus().equals(EBookingStatus.PENDING)) {
            throw new RuntimeException("Payment paid already");
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUser(user);
        log.info("Wallet id: " + wallet.getId());

        Transaction transaction = Transaction.builder()
                .type(ETransactionType.PAYMENT)
                .amount(booking.getTotalPrice())
                .status(ETransactionStatus.PENDING)
                .booking(booking)
                .wallet(wallet)
                .build();

        transactionRepository.save(transaction);

        if (wallet.getBalance().compareTo(transaction.getAmount()) < 0) {
            transaction.setStatus(ETransactionStatus.FAILED);
            transactionRepository.save(transaction);
            log.warn("Balance of userId {} not enough for bookingId ", username,  booking.getId());
            throw new RuntimeException("Not enough balance");
        }

        walletService.debitWallet(wallet.getId(), transaction);

        transaction.setStatus(ETransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
        log.info("Transaction update status: " + transaction.getStatus());

        booking.setBookingStatus(EBookingStatus.PAID);
        bookingRepository.save(booking);
        log.info("Booking {} paid",  booking.getId());

        log.info("wallet paid successfully: userId={}, bookingId={}", username, booking.getId());
    }

    @Transactional
    @Override
    public void refundBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));

        User user = booking.getUser();

        Wallet wallet = walletRepository.findByUser(user);

        if (!booking.getBookingStatus().equals(EBookingStatus.PAID)) {
            throw new RuntimeException("Invalid booking status");
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
            throw new RuntimeException("Refund failed: " + e.getMessage());
        }
    }
}
