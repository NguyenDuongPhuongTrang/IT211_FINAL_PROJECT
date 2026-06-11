package com.example.it211project.service;

import com.example.it211project.enums.BookingStatus;
import com.example.it211project.model.dto.request.BookingRequest;
import com.example.it211project.model.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingRequest request);
    BookingResponse getBookingById(Long bookingId);
    List<BookingResponse> getMyBookings(Long userId);
    BookingResponse updateStatus(Long bookingId, BookingStatus status);
}