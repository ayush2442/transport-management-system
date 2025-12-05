package com.tms.transport_management_system.service;

import com.tms.transport_management_system.DTO.BestBidDTO;
import com.tms.transport_management_system.DTO.BidDTO;
import com.tms.transport_management_system.DTO.LoadDTO;
import com.tms.transport_management_system.DTO.LoadWithBidsDTO;
import com.tms.transport_management_system.entity.Bid;
import com.tms.transport_management_system.entity.Load;
import com.tms.transport_management_system.entity.Transporter;
import com.tms.transport_management_system.enums.BidStatus;
import com.tms.transport_management_system.enums.LoadStatus;
import com.tms.transport_management_system.exception.InvalidStatusTransitionException;
import com.tms.transport_management_system.exception.ResourceNotFoundException;
import com.tms.transport_management_system.repository.BidRepository;
import com.tms.transport_management_system.repository.BookingRepository;
import com.tms.transport_management_system.repository.LoadRepository;
import com.tms.transport_management_system.repository.TransporterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoadService {

    private final LoadRepository loadRepository;
    private final BidRepository bidRepository;
    private final BookingRepository bookingRepository;
    private final TransporterRepository transporterRepository;

    @Transactional
    public LoadDTO createLoad(LoadDTO loadDTO) {
        Load load = new Load();
        load.setShipperId(loadDTO.getShipperId());
        load.setLoadingCity(loadDTO.getLoadingCity());
        load.setUnloadingCity(loadDTO.getUnloadingCity());
        load.setLoadingDate(loadDTO.getLoadingData());
        load.setProductType(loadDTO.getProductType());
        load.setWeight(loadDTO.getWeight());
        load.setWeightUnit(loadDTO.getWeightUnit());
        load.setTruckType(loadDTO.getTruckType());
        load.setNoOfTrucks(loadDTO.getNoOfTrucks());
        load.setStatus(LoadStatus.POSTED);

        load = loadRepository.save(load);
        return mapToDTO(load);
    }

    public Page<LoadDTO> getLoads(String shipperId, LoadStatus status, Pageable pageable) {
        Page<Load> loads;

        if (shipperId != null && status != null) {
            loads = loadRepository.findByShipperIdAndStatus(shipperId, status, pageable);
        } else if (shipperId != null) {
            loads = loadRepository.findByShipperId(shipperId, pageable);
        } else if (status != null) {
            loads = loadRepository.findByStatus(status, pageable);
        } else {
            loads = loadRepository.findAll(pageable);
        }

        return loads.map(this::mapToDTO);
    }

    public LoadWithBidsDTO getLoadWithBids(UUID loadId) {
        Load load = loadRepository.findById(loadId).orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));

        List<Bid> activeBids = bidRepository.findByLoadIdAndStatus(loadId, BidStatus.PENDING);

        LoadWithBidsDTO dto = new LoadWithBidsDTO();
        dto.setLoad(mapToDTO(load));
        dto.setActiveBids(activeBids.stream().map(this::mapBidToDTO).collect(Collectors.toList()));

        return dto;
    }

    @Transactional
    public LoadDTO cancelLoad(UUID loadId) {
        Load load = loadRepository.findById(loadId).orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));

        if (load.getStatus() == LoadStatus.BOOKED) {
            throw new InvalidStatusTransitionException("Cannot cancel a load that is already booked");
        }

        load.setStatus(LoadStatus.CANCELLED);
        load = loadRepository.save(load);

        return mapToDTO(load);
    }

    public List<BestBidDTO> getBestBids(UUID loadId) {
        Load load = loadRepository.findById(loadId).orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));

        List<Bid> pendingBids = bidRepository.findByLoadIdAndStatus(loadId, BidStatus.PENDING);

        return pendingBids.stream().map(bid -> {
            Transporter transporter = transporterRepository.findById(bid.getTransporterId()).orElseThrow(() -> new ResourceNotFoundException("Transporter not found"));

            double score = (1.0 / bid.getProposedRate()) * 0.7 + (transporter.getRating() / 5.0) * 0.3;

            BestBidDTO bestBid = new BestBidDTO();
            bestBid.setBid(mapBidToDTO(bid));
            bestBid.setScore(score);
            bestBid.setTransporterRating(transporter.getRating());

            return bestBid;
        })
                .sorted(Comparator.comparingDouble(BestBidDTO::getScore).reversed())
                .collect(Collectors.toList());
    }

    private LoadDTO mapToDTO(Load load) {
        LoadDTO dto = new LoadDTO();
        dto.setLoadId(load.getLoadId());
        dto.setShipperId(load.getShipperId());
        dto.setLoadingCity(load.getLoadingCity());
        dto.setUnloadingCity(load.getUnloadingCity());
        dto.setLoadingData(load.getLoadingDate());
        dto.setProductType(load.getProductType());
        dto.setWeight(load.getWeight());
        dto.setWeightUnit(load.getWeightUnit());
        dto.setTruckType(load.getTruckType());
        dto.setNoOfTrucks(load.getNoOfTrucks());
        dto.setStatus(load.getStatus());
        dto.setDatePosted(load.getDatePosted());

        return dto;
    }

    private BidDTO mapBidToDTO(Bid bid) {
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
