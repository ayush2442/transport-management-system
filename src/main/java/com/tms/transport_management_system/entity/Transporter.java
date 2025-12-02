package com.tms.transport_management_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transporters", indexes = {
        @Index(name = "idx_transporter_rating", columnList = "rating")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transporter {

    private UUID transporterId;

    private String companyName;

    private Double rating = 3.0;

    private List<TruckCapacity> availableTrucks = new ArrayList<>();

}
