package com.example.it211project.service.impl;

import com.example.it211project.enums.BookingStatus;
import com.example.it211project.exception.ConflictException;
import com.example.it211project.exception.ResourceNotFoundException;
import com.example.it211project.model.dto.request.BookingRequest;
import com.example.it211project.model.dto.response.BookingResponse;
import com.example.it211project.model.entity.Booking;
import com.example.it211project.model.entity.Court;
import com.example.it211project.model.entity.User;
import com.example.it211project.repository.BookingRepository;
import com.example.it211project.repository.CourtRepository;
import com.example.it211project.repository.UserRepository;
import com.example.it211project.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;

    @Override
    public BookingResponse createBooking(Long userId, BookingRequest request) {
        boolean existed = bookingRepository.existsByCourtIdAndBookingDateAndTimeSlot(request.getCourtId(), request.getBookingDate(), request.getTimeSlot());

        if (existed) {
            throw new ConflictException("Sân đã được đặt");
        }

        User customer = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User"));
        Court court = courtRepository.findById(request.getCourtId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sân"));

        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setCourt(court);
        booking.setBookingDate(request.getBookingDate());
        booking.setTimeSlot(request.getTimeSlot());
        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);

        return mapToResponse(booking);
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getMyBookings(Long userId) {
        return bookingRepository
                .findByCustomerId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BookingResponse updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt sân"));
        booking.setStatus(status);
        bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .customerName(booking.getCustomer().getUsername())
                .courtId(booking.getCourt().getId())
                .courtName(booking.getCourt().getCourtName())
                .bookingDate(booking.getBookingDate())
                .timeSlot(booking.getTimeSlot())
                .status(booking.getStatus())
                .build();
    }
}
