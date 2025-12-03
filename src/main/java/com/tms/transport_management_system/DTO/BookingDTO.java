package com.tms.transport_management_system.DTO;

import com.tms.transport_management_system.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private UUID bookingId;

    @NotNull(message = "Bid ID is required")
    private UUID bidId;

    private UUID loadID;
    private UUID transporterId;
    private Integer allocatedTrucks;
    private Double finalRate;
    private BookingStatus status;
    private LocalDateTime bookedAt;

}
