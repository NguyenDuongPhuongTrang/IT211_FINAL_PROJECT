package com.example.it211project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull(message = "Court ID không được để trống")
    private Long courtId;

    @NotNull(message = "Date không được để trống")
    private LocalDate bookingDate;

    @NotBlank(message = "Time Slot không được để trống")
    private String timeSlot;
}
