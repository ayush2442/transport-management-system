package com.tms.transport_management_system.controller;

import com.tms.transport_management_system.DTO.BookingDTO;
import com.tms.transport_management_system.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createdBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        BookingDTO created = bookingService.createBooking(bookingDTO);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable UUID bookingId) {
        BookingDTO booking = bookingService.getBooking(bookingId);

        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable UUID bookingId) {
        BookingDTO cancelled = bookingService.cancelBooking(bookingId);

        return ResponseEntity.ok(cancelled);
    }

}
