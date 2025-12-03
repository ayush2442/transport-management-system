package com.tms.transport_management_system.entity;

import com.tms.transport_management_system.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_load", columnList = "loadId"),
        @Index(name = "idx_booking_transporter", columnList = "transporterId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID loadId;

    @Column(nullable = false)
    private UUID bidId;

    @Column(nullable = false)
    private UUID transporterId;

    @Column(nullable = false)
    private Integer allocatedTrucks;

    @Column(nullable = false)
    private Double finalRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime bookedAt;

}
