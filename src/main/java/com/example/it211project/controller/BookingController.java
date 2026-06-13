package com.example.it211project.controller;

import com.example.it211project.model.dto.request.BookingRequest;
import com.example.it211project.model.dto.request.UpdateBookingStatusRequest;
import com.example.it211project.model.dto.response.ApiResponse;
import com.example.it211project.model.dto.response.BookingResponse;
import com.example.it211project.model.entity.User;
import com.example.it211project.repository.UserRepository;
import com.example.it211project.service.BookingService;
import com.example.it211project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserEntityByUsername(username);

        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Đặt sân thành công",
                bookingService.createBooking(user.getId(), request)
        ), HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy thông tin lịch đặt thành công",
                bookingService.getBookingById(id)
        ), HttpStatus.OK);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> myBookings(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserEntityByUsername(username);

        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lấy lịch đặt thành công",
                bookingService.getMyBookings(user.getId())
        ), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateBookingStatusRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cập nhật trạng thái thành công",
                bookingService.updateStatus(id, request.getStatus())
        ), HttpStatus.OK);
    }
}
