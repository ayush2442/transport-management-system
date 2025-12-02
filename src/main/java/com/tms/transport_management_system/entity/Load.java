package com.tms.transport_management_system.entity;

import com.tms.transport_management_system.enums.LoadStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loads", indexes = {
        @Index(name = "idx_load_status", columnList = "status"),
        @Index(name = "idx_shipper_id", columnList = "shipperId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Load {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loadId;

    @Column(nullable = false)
    private String shipperId;

    @Column(nullable = false)
    private String loadingCity;

    @Column(nullable = false)
    private String unloadingCity;

    @Column(nullable = false)
    private LocalDateTime loadingDate;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeightUnit weightUnit;

    @Column(nullable = false)
    private String  truckType;

    @Column(nullable = false)
    private Integer noOfTrucks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoadStatus status = LoadStatus.POSTED;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime datePosted;

    private Long version;

}
