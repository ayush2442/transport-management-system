package com.tms.transport_management_system.service;

import com.tms.transport_management_system.DTO.BidDTO;
import com.tms.transport_management_system.entity.Bid;
import com.tms.transport_management_system.entity.Load;
import com.tms.transport_management_system.entity.Transporter;
import com.tms.transport_management_system.entity.TruckCapacity;
import com.tms.transport_management_system.enums.BidStatus;
import com.tms.transport_management_system.enums.LoadStatus;
import com.tms.transport_management_system.exception.InsufficientCapacityException;
import com.tms.transport_management_system.exception.InvalidStatusTransitionException;
import com.tms.transport_management_system.exception.ResourceNotFoundException;
import com.tms.transport_management_system.repository.BidRepository;
import com.tms.transport_management_system.repository.LoadRepository;
import com.tms.transport_management_system.repository.TransporterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;

    @Transactional
    public BidDTO createdBid(BidDTO bidDTO) {
        Load load = loadRepository.findById(bidDTO.getLoadId()).orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + bidDTO.getLoadId()));

        if (load.getStatus() == LoadStatus.CANCELLED || load.getStatus() == LoadStatus.BOOKED) {
            throw new InvalidStatusTransitionException("Cannot bid on " + load.getStatus() + " loads");
        }

        Transporter transporter = transporterRepository.findById(bidDTO.getTransporterId()).orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + bidDTO.getTransporterId()));

        TruckCapacity availableCapacity = transporter.getAvailableTrucks().stream()
                .filter(tc -> tc.getTruckType().equals(load.getTruckType()))
                .findFirst()
                .orElseThrow(() -> new InsufficientCapacityException("Transporter does not have " + load.getTruckType() + " trucks"));

        if (availableCapacity.getCount() < bidDTO.getTrucksOffered()) {
            throw new InsufficientCapacityException(
                    "Insufficient capacity. Available: " + availableCapacity.getCount() + ", Requested: " + bidDTO.getTrucksOffered()
            );
        }

        Bid bid = new Bid();
        bid.setLoadId(bidDTO.getLoadId());
        bid.setTransporterId(bidDTO.getTransporterId());
        bid.setProposedRate((bidDTO.getProposedRate()));
        bid.setTrucksOffered(bidDTO.getTrucksOffered());
        bid.setStatus(BidStatus.PENDING);

        bid = bidRepository.save(bid);

        if (load.getStatus() == LoadStatus.POSTED) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
            loadRepository.save(load);
        }

        return mapToDTO(bid);
    }

    public List<BidDTO> getBids(UUID loadId, UUID transporterId, BidStatus status) {
        List<Bid> bids = bidRepository.findByFilters(loadId, transporterId, status);

        return bids.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public BidDTO getBid(UUID bidId) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bidId));

        return mapToDTO(bid);
    }

    public BidDTO rejectBid(UUID bidId) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bidId));

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusTransitionException("Can only reject PENDING bids");
        }

        bid.setStatus(BidStatus.REJECTED);
        bid = bidRepository.save(bid);

        return mapToDTO(bid);
    }

    private BidDTO mapToDTO(Bid bid) {
        BidDTO dto = new BidDTO();
        dto.setBidId(bid.getBidId());
        dto.setLoadId(bid.getLoadId());
        dto.setTransporterId(bid.getTransporterId());
        dto.setProposedRate(bid.getProposedRate());
        dto.setTrucksOffered(bid.getTrucksOffered());
        dto.setStatus(bid.getStatus());
        dto.setSubmittedAt(bid.getSubmittedAt());

        return dto;
    }

}
