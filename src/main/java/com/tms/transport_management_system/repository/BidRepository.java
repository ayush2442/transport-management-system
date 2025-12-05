package com.tms.transport_management_system.repository;

import com.tms.transport_management_system.entity.Bid;
import com.tms.transport_management_system.enums.BidStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository {

    List<Bid> findByLoadId(UUID loadId);

    List<Bid> findByTransporterId(UUID transporterId);

    List<Bid> findByStatus(BidStatus status);

    List<Bid> findByLoadIdAndStatus(UUID loadId, BidStatus status);

    List<Bid> findByTransporterIdAndStatus(UUID transporterId, BidStatus status);

    List<Bid> findByLoadIdAndTransporterId(UUID loadId, UUID transporterId);

    @Query("SELECT b FROM Bid b WHERE " +
            "(:loadId IS NULL OR b.loadId = :loadId) AND " +
            "(:transporterId IS NULL OR b.transporterId = :transporterId) AND " +
            "(:status IS NULL OR b.status = :status)")

    List<Bid> findByFilters(
            @Param("loadId") UUID loadId,
            @Param("transporterId") UUID transporterId,
            @Param("status") BidStatus status
    );
}
