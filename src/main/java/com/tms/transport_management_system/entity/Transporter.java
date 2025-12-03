package com.tms.transport_management_system.entity;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transporterId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Double rating = 3.0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "transporter_truck",
            joinColumns = @JoinColumn(name = "transporter_id")
    )
    private List<TruckCapacity> availableTrucks = new ArrayList<>();

}
