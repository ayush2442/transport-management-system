package com.tms.transport_management_system.DTO;

import com.tms.transport_management_system.entity.TruckCapacity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransporterDTO {

    private UUID transporterId;

    @NotNull(message = "Company name is required")
    private String companyName;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Double rating;

    @NotNull(message = "Available trucks are required")
    @Size(min = 1, message = "At least one truck type is required")
    private List<TruckCapacity> availableTrucks;

}
