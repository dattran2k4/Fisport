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
import com.Fisport.service.PaymentService;
import com.Fisport.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final VnPayService vnPayService;
    private final PaymentRepository paymentRepository;

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
            default:
                throw new ResourceNotFoundException("Payment method not supported");
        }
    }

    @Override
    public PaymentResponse handleVnpayReturn(Map<String, String> params) {
        boolean valid = vnPayService.validatePayment(params);
        if (!valid) {
            throw new RuntimeException("Invalid VNPay callback");
        }

        String transactionNo = params.get("vnp_TransactionNo");

        Long bookingId = vnPayService.extractBookingId(params);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

//        Payment payment = new Payment();
//        payment.setBooking(booking);
//        payment.setAmount(booking.getTotalPrice());
//        payment.setMethod(EPaymentMethod.VNPAY);
//        payment.setStatus(EPaymentStatus.PENDING);
//        payment.setTransactionId(transactionNo);

        EPaymentStatus paymentStatus = EPaymentStatus.PENDING;

        String response = params.get("vnp_ResponseCode");
        if ("00".equals(response)) {
            paymentStatus = EPaymentStatus.SUCCESS;

            Payment payment = Payment.builder()
                    .booking(booking)
                    .amount(booking.getTotalPrice())
                    .method(EPaymentMethod.VNPAY)
                    .status(EPaymentStatus.SUCCESS)
                    .transactionId(transactionNo)
                    .paymentTime(LocalDateTime.now())
                    .build();

            booking.setBookingStatus(EBookingStatus.PAID);
            paymentRepository.save(payment);
        } else if("99".equals(response)) {
            paymentStatus =  EPaymentStatus.FAILED;
            booking.setBookingStatus(EBookingStatus.FAILED);
        } else if ("24".equals(response)) {
            paymentStatus =  EPaymentStatus.FAILED;
            booking.setBookingStatus(EBookingStatus.CANCELLED);
        }

        bookingRepository.save(booking);

        return PaymentResponse.builder()
                .status(paymentStatus)
                .amount(booking.getTotalPrice())
                .method(EPaymentMethod.VNPAY)
                .transactionId(transactionNo)
                .paymentAt(paymentStatus == EPaymentStatus.SUCCESS ? LocalDateTime.now() : null)
                .build();
    }

    @Override
    public Booking findByPaymentToken(String paymentToken) {
        return bookingRepository.findByPaymentTokenAndBookingStatus(paymentToken, List.of(EBookingStatus.PENDING, EBookingStatus.FAILED))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found or already paid"));
    }
}
