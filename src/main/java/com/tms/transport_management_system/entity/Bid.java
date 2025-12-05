package com.tms.transport_management_system.entity;

import com.tms.transport_management_system.enums.BidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bids", indexes = {
        @Index(name = "idx_bid_load", columnList = "loadId"),
        @Index(name = "idx_bid_transporter", columnList = "transporterId"),
        @Index(name = "idx_bid_status", columnList = "status")
        },
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_one_accepted_bid_per_load",
                    columnNames = {"loadId", "status"}
            )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bidId;

    @Column(nullable = false)
    private UUID loadId;

    @Column(nullable = false)
    private UUID transporterId;

    @Column(nullable = false)
    private Double proposedRate;

    @Column(nullable = false)
    private Integer trucksOffered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status = BidStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

}
