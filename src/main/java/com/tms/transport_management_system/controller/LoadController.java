package com.tms.transport_management_system.controller;

import com.tms.transport_management_system.DTO.BestBidDTO;
import com.tms.transport_management_system.DTO.LoadDTO;
import com.tms.transport_management_system.DTO.LoadWithBidsDTO;
import com.tms.transport_management_system.enums.LoadStatus;
import com.tms.transport_management_system.service.LoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/load")
@RequiredArgsConstructor
public class LoadController {

    private final LoadService loadService;

    @PostMapping
    public ResponseEntity<LoadDTO> createLoad(@Valid @RequestBody LoadDTO loadDTO) {
        LoadDTO created = loadService.createLoad(loadDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<LoadDTO>> getLoads(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false)LoadStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LoadDTO> loads = loadService.getLoads(shipperId, status, pageable);

        return ResponseEntity.ok(loads);
    }

    @GetMapping("/{loadId}")
    public ResponseEntity<LoadWithBidsDTO> getLoad(@PathVariable UUID loadId) {
        LoadWithBidsDTO load = loadService.getLoadWithBids(loadId);

        return ResponseEntity.ok(load);
    }

    @PatchMapping("/{loadId}/cancel")
    public ResponseEntity<LoadDTO> cancelLoad(@PathVariable UUID loadId) {
        LoadDTO cancelled = loadService.cancelLoad(loadId);

        return ResponseEntity.ok(cancelled);
    }

    @GetMapping("/{loadId}/best-bids")
    public ResponseEntity<List<BestBidDTO>> getBestBids(@PathVariable UUID loadId) {
        List<BestBidDTO> bestBids = loadService.getBestBids(loadId);

        return ResponseEntity.ok(bestBids);
    }

}
