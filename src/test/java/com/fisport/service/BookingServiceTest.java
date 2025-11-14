package com.fisport.service;

import com.fisport.common.ESubFieldStatus;
import com.fisport.dto.request.BookingRequest;
import com.fisport.exception.InvalidDataException;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.*;
import com.fisport.repository.*;
import com.fisport.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubFieldRepository subFieldRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FieldHasTimeSlotRepository fieldHasTimeSlotRepository;

    @Mock
    private FieldServiceItemRepository fieldServiceItemRepository;

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private FieldHasTimeSlotService fieldHasTimeSlotService;

    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequest request;
    private User user;
    private SubField subField;
    private Field field;
    private FieldType fieldType;

    @BeforeEach
    void setUp() {

        request = new BookingRequest();
        request.setSubFieldId(1L);
        request.setDate(LocalDate.now().plusDays(1)); // Ngày mai
        request.setStartTime(LocalTime.of(9, 0));  // 9:00
        request.setEndTime(LocalTime.of(10, 0));    // 10:00
        request.setDuration(60);

        user = new User();
        user.setId(100L);

        field = new Field();
        field.setOpenTime(LocalTime.of(6, 0));
        field.setCloseTime(LocalTime.of(22, 0));

        Duration durationEntity = new  Duration();
        durationEntity.setMinutes(60);

        fieldType = new FieldType();
        FieldTypeBookDuration duration = new FieldTypeBookDuration();
        duration.setDuration(durationEntity);
        fieldType.setFieldTypeBookDuration(Set.of(duration));
        field.setFieldType(fieldType);

        subField = new SubField();
        subField.setId(1L);
        subField.setStatus(ESubFieldStatus.AVAILABLE);
        subField.setField(field);
    }

    @Test
    @DisplayName("Should FAIL to create booking when slot is already booked (Overlap)")
    void testCreateBooking_Fail_SlotAlreadyBooked() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subFieldRepository.findById(1L)).thenReturn(Optional.of(subField));

        when(bookingRepository.findOverlappingBookingsForUpdate(
                request.getSubFieldId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        )).thenReturn(List.of(new Booking())); // Trả về 1 list có 1 phần tử



        // Kiểm tra xem hàm có ném ra đúng lỗi InvalidDataException không
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bookingService.createBooking(request, 100L);
        });

        // (Tùy chọn) Kiểm tra nội dung của message lỗi
        assertEquals("Giờ này trong sân đã bị đặt rồi, bạn chọn giờ khác nhé", exception.getMessage());

        // Quan trọng: Xác minh rằng hàm save KHÔNG BAO GIỜ được gọi
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should FAIL to create booking when SubField is not AVAILABLE")
    void testCreateBooking_Fail_SubFieldNotAvailable() {

        subField.setStatus(ESubFieldStatus.MAINTAIN);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(subFieldRepository.findById(1L)).thenReturn(Optional.of(subField));

        // Kiểm tra xem hàm có ném ra đúng lỗi InvalidDataException không
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bookingService.createBooking(request, 100L); // Gọi hàm cần test
        });

        // Kiểm tra nội dung của message lỗi
        assertEquals("Subfield not available", exception.getMessage());

        // Xác minh rằng hàm save KHÔNG BAO GIỜ được gọi
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should FAIL to create booking when User is not found")
    void testCreateBooking_Fail_UserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(request, 100L);
        });

        // Kiểm tra nội dung của message lỗi
        assertEquals("Không tìm thấy user", exception.getMessage());

        // Đảm bảo rằng hàm save không bao giờ được gọi
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should FAIL to create booking when Date is in the past")
    void testCreateBooking_Fail_DateInPast() {

        request.setDate(LocalDate.now().minusDays(1));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        subField.setStatus(ESubFieldStatus.AVAILABLE);
        when(subFieldRepository.findById(request.getSubFieldId())).thenReturn(Optional.of(subField));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bookingService.createBooking(request, 100L);
        });

        assertEquals("Date must be after now", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should FAIL to create booking when StartTime is before field's open time")
    void testCreateBooking_Fail_StartTimeBeforeOpenTime() {

        request.setStartTime(LocalTime.of(5, 0));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        subField.setStatus(ESubFieldStatus.AVAILABLE);
        when(subFieldRepository.findById(request.getSubFieldId())).thenReturn(Optional.of(subField));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            bookingService.createBooking(request, 100L);
        });

        assertEquals("Thời gian bắt đầu chơi không được trước giờ mở cửa", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
