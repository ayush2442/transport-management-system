package com.tms.transport_management_system.controller;

import com.tms.transport_management_system.DTO.TransporterDTO;
import com.tms.transport_management_system.entity.TruckCapacity;
import com.tms.transport_management_system.service.TransporterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transporter")
@RequiredArgsConstructor
public class TransporterController {

    private final TransporterService transporterService;

    @PostMapping
    public ResponseEntity<TransporterDTO> createTransporter(@Valid @RequestBody TransporterDTO transporterDTO) {
        TransporterDTO created = transporterService.createTransporter(transporterDTO);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{transporterId}")
    public ResponseEntity<TransporterDTO> getTransporter(@PathVariable UUID transporterId) {
        TransporterDTO transporter = transporterService.getTransporter(transporterId);

        return ResponseEntity.ok(transporter);
    }

    @PutMapping("/{transporterId}/trucks")
    public ResponseEntity<TransporterDTO> updateTrucks(@PathVariable UUID transportId, @RequestBody List<TruckCapacity> trucks) {
        TransporterDTO updated = transporterService.updateTrucks(transportId, trucks);

        return ResponseEntity.ok(updated);
    }

}
