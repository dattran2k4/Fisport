package com.Fisport.service.impl;

import com.Fisport.common.EBookingStatus;
import com.Fisport.common.EPaymentMethod;
import com.Fisport.common.EPaymentStatus;
import com.Fisport.dto.request.PaymentRequest;
import com.Fisport.dto.response.PaymentResponse;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Booking;
import com.Fisport.model.Payment;
import com.Fisport.repository.BookingRepository;
import com.Fisport.repository.PaymentRepository;
import com.Fisport.service.PayOSService;
import com.Fisport.service.PaymentService;
import com.Fisport.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.model.webhooks.WebhookData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final VnPayService vnPayService;
    private final PaymentRepository paymentRepository;
    private final PayOSService payOSService;

    @Override
    public String createPayment(PaymentRequest request, HttpServletRequest httpServletRequest) {
        Booking booking = findByPaymentToken(request.getPaymentToken());

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
    public PaymentResponse handleVnpayReturn(Map<String, String> params) {
        boolean valid = vnPayService.validatePayment(params);
        if (!valid) {
            throw new RuntimeException("Invalid VNPay callback");
        }


        Long bookingId = vnPayService.extractBookingId(params);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String response = params.get("vnp_ResponseCode");
        String transactionNo = Optional.ofNullable(params.get("vnp_TransactionNo")).orElse("UNKNOWN");

        EPaymentStatus paymentStatus;
        EBookingStatus bookingStatus = EBookingStatus.PENDING;
        LocalDateTime paymentTime = null;
        switch (response) {
            case "00":
                paymentStatus = EPaymentStatus.SUCCESS;
                bookingStatus = EBookingStatus.PAID;
                paymentTime = LocalDateTime.now();
                break;
            case "24":
                paymentStatus = EPaymentStatus.CANCELLED;
                break;
            default:
                paymentStatus = EPaymentStatus.FAILED;
                break;
        }

        booking.setBookingStatus(bookingStatus);
        bookingRepository.save(booking);

        Payment payment = Payment.builder()
                .booking(booking)
                .paymentTime(paymentTime)
                .amount(booking.getTotalPrice())
                .method(EPaymentMethod.VNPAY)
                .status(paymentStatus)
                .transactionId(transactionNo)
                .build();
        paymentRepository.save(payment);

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

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setMethod(EPaymentMethod.PAYOS);

        if (data.getCode().equals("00") && data.getDesc().equals("success")) {
            booking.setBookingStatus(EBookingStatus.PAID);
            payment.setStatus(EPaymentStatus.SUCCESS);
            payment.setTransactionId(data.getReference());
            payment.setPaymentTime(LocalDateTime.now());
            payment.setBooking(booking);
        } else {
            payment.setStatus(EPaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
        bookingRepository.save(booking);
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
                    .transactionId(paymentRepository.findByBooking(booking).getTransactionId())
                    .build();
        } else {
            return PaymentResponse.builder()
                    .status(EPaymentStatus.CANCELLED)
                    .amount(booking.getTotalPrice())
                    .method(EPaymentMethod.PAYOS)
                    .build();
        }

    }
}
