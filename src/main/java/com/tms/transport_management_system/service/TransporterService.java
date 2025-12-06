package com.tms.transport_management_system.service;

import com.tms.transport_management_system.DTO.TransporterDTO;
import com.tms.transport_management_system.entity.Transporter;
import com.tms.transport_management_system.entity.TruckCapacity;
import com.tms.transport_management_system.exception.ResourceNotFoundException;
import com.tms.transport_management_system.repository.TransporterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransporterService {

    private final TransporterRepository transporterRepository;

    @Transactional
    public TransporterDTO createTransporter(TransporterDTO dto) {
        Transporter transporter = new Transporter();
        transporter.setCompanyName(dto.getCompanyName());
        transporter.setRating(dto.getRating() != null ? dto.getRating() : 3.0);
        transporter.setAvailableTrucks(dto.getAvailableTrucks());

        transporter = transporterRepository.save(transporter);

        return mapToDTO(transporter);
    }

    public TransporterDTO getTransporter(UUID transporterId) {
        Transporter transporter = transporterRepository.findById(transporterId).orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + transporterId));

        return mapToDTO(transporter);
    }

    @Transactional
    public TransporterDTO updateTrucks(UUID transporterId, List<TruckCapacity> trucks) {
        Transporter transporter = transporterRepository.findById(transporterId).orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + transporterId));

        transporter.setAvailableTrucks(trucks);
        transporter = transporterRepository.save(transporter);

        return mapToDTO(transporter);
    }

    private TransporterDTO mapToDTO(Transporter transporter) {
        TransporterDTO dto = new TransporterDTO();
        dto.setTransporterId(transporter.getTransporterId());
        dto.setCompanyName(transporter.getCompanyName());
        dto.setRating(transporter.getRating());
        dto.setAvailableTrucks(transporter.getAvailableTrucks());

        return dto;
    }

}
