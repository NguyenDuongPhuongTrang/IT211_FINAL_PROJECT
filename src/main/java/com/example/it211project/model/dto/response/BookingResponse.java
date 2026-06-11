package com.example.it211project.model.dto.response;

import com.example.it211project.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long courtId;
    private String courtName;
    private LocalDate bookingDate;
    private String timeSlot;
    private BookingStatus status;
}
