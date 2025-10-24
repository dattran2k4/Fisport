package com.Fisport.service.impl;

import com.Fisport.common.*;
import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.dto.request.WalletTopUpRequest;
import com.Fisport.dto.response.PaymentResponse;
import com.Fisport.dto.response.UserResponse;
import com.Fisport.exception.InvalidDataException;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.*;
import com.Fisport.repository.BookingRepository;
import com.Fisport.repository.PaymentRepository;
import com.Fisport.repository.TransactionRepository;
import com.Fisport.repository.WalletRepository;
import com.Fisport.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.model.webhooks.WebhookData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final VnPayService vnPayService;
    private final PaymentRepository paymentRepository;
    private final PayOSService payOSService;
    private final BookingService bookingService;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    public String createPayment(PaymentRequest request, HttpServletRequest httpServletRequest) {
        Booking booking = findByPaymentToken(request.getPaymentToken());
        booking.setPaymentMethod(request.getPaymentMethod());

        bookingService.checkExpiredBooking(booking);
        bookingRepository.save(booking);
        Payment payment = Payment.builder()
                .amount(booking.getTotalPrice())
                .method(request.getPaymentMethod())
                .booking(booking)
                .status(EPaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        Transaction transaction = Transaction.builder()
                .amount(booking.getTotalPrice())
                .status(ETransactionStatus.PENDING)
                .type(ETransactionType.PAYMENT)
                .payment(payment)
                .method(request.getPaymentMethod())
                .build();

        payment.setTransaction(transaction);
        paymentRepository.save(payment);
        transactionRepository.save(transaction);

        switch (request.getPaymentMethod()) {
            case VNPAY:
                return vnPayService.createPaymentUrl(booking, httpServletRequest);
            case MOMO:
                throw new UnsupportedOperationException("Momo payment not implemented");
            case ZALOPAY:
                throw new UnsupportedOperationException("Zalopay payment not implemented");
            case PAYOS:
                return payOSService.createPaymentLink(booking);
            default:
                throw new ResourceNotFoundException("Payment method not supported");
        }
    }

    @Override
    @Transactional
    public String createWalletPayment(WalletTopUpRequest request, HttpServletRequest httpServletRequest) {

        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .method(request.getPaymentMethod())
                .createAt(LocalDateTime.now())
                .status(EPaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);
        log.info("Payment id {} created", payment.getId());

        Wallet wallet = walletRepository.findById(request.getWalletId()).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(ETransactionType.TOUP)
                .payment(payment)
                .createdAt(LocalDateTime.now())
                .status(ETransactionStatus.PENDING)
                .method(request.getPaymentMethod())
                .wallet(wallet)
                .build();
        transactionRepository.save(transaction);
        log.info("Transaction id {} created", transaction.getId());

        switch (request.getPaymentMethod()) {
            case VNPAY:
                return vnPayService.createWalletPaymentUrl(payment, httpServletRequest);
            case MOMO:
                throw new UnsupportedOperationException("Momo payment not implemented");
            case ZALOPAY:
                throw new UnsupportedOperationException("Zalopay payment not implemented");
            case PAYOS:
                return payOSService.createWalletPaymentLink(payment);
            default:
                throw new ResourceNotFoundException("Payment method not supported");
        }
    }

    @Override
    @Transactional
    public PaymentResponse handleVnpayReturn(Map<String, String> params) {
        boolean valid = vnPayService.validatePayment(params);
        if (!valid) {
            throw new InvalidDataException("Invalid VNPay callback");
        }


        Long bookingId = vnPayService.extractId(params);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking"));

        Payment payment = paymentRepository.findByBooking(booking);

        Transaction transaction = transactionRepository.findByPayment(payment).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch"));

        String response = params.get("vnp_ResponseCode");
        String transactionNo = Optional.ofNullable(params.get("vnp_TransactionNo")).orElse("UNKNOWN");

        EPaymentStatus paymentStatus;
        EBookingStatus bookingStatus = EBookingStatus.PENDING;
        ETransactionStatus transactionStatus;
        LocalDateTime paymentTime = null;
        switch (response) {
            case "00":
                paymentStatus = EPaymentStatus.SUCCESS;
                bookingStatus = EBookingStatus.PAID;
                transactionStatus = ETransactionStatus.SUCCESS;
                paymentTime = LocalDateTime.now();
                payment.setTransactionCode(transactionNo);
                log.info("Payment SUCCESS for booking {} | TransactionNo={} | Amount={} | Time={}",
                        bookingId, transactionNo, booking.getTotalPrice(), paymentTime);
                break;
            case "24":
                paymentStatus = EPaymentStatus.CANCELLED;
                transactionStatus = ETransactionStatus.FAILED;
                break;
            default:
                paymentStatus = EPaymentStatus.FAILED;
                transactionStatus = ETransactionStatus.FAILED;
                break;
        }

        booking.setBookingStatus(bookingStatus);
        bookingRepository.save(booking);

        payment.setStatus(paymentStatus);
        payment.setPaymentTime(paymentTime);
        paymentRepository.save(payment);

        transaction.setStatus(transactionStatus);
        transactionRepository.save(transaction);

        log.info("Booking {}, Payment {}, Transaction {} updated with status: {} / {} / {}",
                booking.getId(),
                payment.getId(),
                transaction.getId(),
                bookingStatus,
                paymentStatus,
                transactionStatus
        );

        return PaymentResponse.builder()
                .amount(booking.getTotalPrice())
                .method(EPaymentMethod.VNPAY)
                .paymentAt(paymentTime)
                .transactionId(transactionNo)
                .status(paymentStatus)
                .build();
    }

    @Override
    @Transactional
    public void handlePayOSWebHook(WebhookData data) {
        Long bookingId = data.getOrderCode() % 100000;

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = paymentRepository.findByBooking(booking);

        Transaction transaction = transactionRepository.findByPayment(payment).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (data.getCode().equals("00") && data.getDesc().equals("success")) {
            booking.setBookingStatus(EBookingStatus.PAID);
            payment.setStatus(EPaymentStatus.SUCCESS);
            payment.setTransactionCode(data.getReference());
            payment.setPaymentTime(LocalDateTime.now());
            transaction.setStatus(ETransactionStatus.SUCCESS);
            log.info("PayOS payment SUCCESS for booking={} | ref={} | amount={} | time={}",
                    bookingId, data.getReference(), booking.getTotalPrice(), LocalDateTime.now());
        } else {
            payment.setStatus(EPaymentStatus.FAILED);
            transaction.setStatus(ETransactionStatus.FAILED);
        }

        transactionRepository.save(transaction);
        paymentRepository.save(payment);
        bookingRepository.save(booking);

        log.info("Booking {}, Payment {}, Transaction {} updated with status: {} / {} / {}",
                booking.getId(),
                payment.getId(),
                transaction.getId(),
                booking.getBookingStatus(),
                payment.getStatus(),
                transaction.getStatus()
        );
    }


    @Override
    public Booking findByPaymentToken(String paymentToken) {
        return bookingRepository.findByPaymentTokenAndBookingStatus(paymentToken, List.of(EBookingStatus.PENDING, EBookingStatus.FAILED))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found or already paid"));
    }

    @Override
    public Booking findByOrderCodePayOs(long orderCode) {
        Long bookingId = orderCode % 100000;

        return bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public PaymentResponse checkPaymentPayOSView(long orderCode, String status) {
        Booking booking = findByOrderCodePayOs(orderCode);

        if (status.equals("SUCCESS")) {
            return PaymentResponse.builder()
                    .status(EPaymentStatus.SUCCESS)
                    .amount(booking.getTotalPrice())
                    .method(EPaymentMethod.PAYOS)
                    .transactionId(paymentRepository.findByBooking(booking).getTransactionCode())
                    .build();
        } else {
            return PaymentResponse.builder()
                    .status(EPaymentStatus.CANCELLED)
                    .amount(booking.getTotalPrice())
                    .method(EPaymentMethod.PAYOS)
                    .build();
        }

    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    @Override
    public Payment findPaymentByOrderCodePayOs(long orderCode) {
        Long paymentId = orderCode % 100000;
        return getPaymentById(paymentId);
    }
}
