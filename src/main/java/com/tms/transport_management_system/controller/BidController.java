package com.tms.transport_management_system.controller;

import com.tms.transport_management_system.DTO.BidDTO;
import com.tms.transport_management_system.enums.BidStatus;
import com.tms.transport_management_system.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidDTO> createBid(@Valid @RequestBody BidDTO bidDTO) {
        BidDTO created = bidService.createdBid(bidDTO);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BidDTO>> getBids(
            @RequestParam(required = false) UUID loadId,
            @RequestParam(required = false) UUID transporterId,
            @RequestParam(required = false)BidStatus status
            ) {
        List<BidDTO> bids = bidService.getBids(loadId, transporterId, status);

        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<BidDTO> getBid(@PathVariable UUID bidId) {
        BidDTO bid = bidService.getBid(bidId);

        return ResponseEntity.ok(bid);
    }

    @GetMapping("/{bidId}/reject")
    public ResponseEntity<BidDTO> rejectBid(@PathVariable UUID bidId) {
        BidDTO rejected = bidService.rejectBid(bidId);

        return ResponseEntity.ok(rejected);
    }

}
