package com.tms.transport_management_system.DTO;

import com.tms.transport_management_system.enums.LoadStatus;
import com.tms.transport_management_system.enums.WeightUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadDTO {

    private UUID loadId;

    @NotBlank(message = "Shipper ID is required")
    private String shipperId;

    @NotBlank(message = "Loading city is required")
    private String loadingCity;

    @NotBlank(message = "Unloading city is required")
    private String unloadingCity;

    @NotBlank(message = "Loading date is required")
    private LocalDateTime loadingData;

    @NotBlank(message = "Product type is required")
    private String productType;

    @NotBlank(message = "Weight is required")
    @Min(value = 1, message = "Weight must be greater than 0")
    private Double weight;

    @NotBlank(message = "Weight unit is required")
    private WeightUnit weightUnit;

    @NotNull(message = "Number of trucks is required")
    @Min(value = 1, message = "Number of trucks must be at least 1")
    private String truckType;

    private Integer noOfTrucks;

    private LoadStatus status;

    private LocalDateTime datePosted;

}
