package com.tms.transport_management_system.DTO;

import com.tms.transport_management_system.enums.BidStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO {

    private UUID bidId;

    @NotNull(message = "Load ID is required")
    private UUID loadId;

    @NotNull(message = "Transporter ID is required")
    private UUID transporterId;

    @NotNull(message = "Proposed rate is required")
    @Min(value = 1, message = "Proposed rate must be greater than 0")
    private Double proposedRate;

    @NotNull(message = "Trucks offered is required")
    @Min(value = 1, message = "Trucks offered must be at least 1")
    private Integer trucksOffered;

    private BidStatus status;
    private LocalDateTime submittedAt;
    
}
