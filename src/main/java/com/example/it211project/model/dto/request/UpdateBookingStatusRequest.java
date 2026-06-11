package com.example.it211project.model.dto.request;

import com.example.it211project.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private BookingStatus status;
}
