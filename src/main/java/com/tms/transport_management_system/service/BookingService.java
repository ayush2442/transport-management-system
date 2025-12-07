package com.tms.transport_management_system.service;

import com.tms.transport_management_system.DTO.BookingDTO;
import com.tms.transport_management_system.entity.*;
import com.tms.transport_management_system.enums.BidStatus;
import com.tms.transport_management_system.enums.BookingStatus;
import com.tms.transport_management_system.enums.LoadStatus;
import com.tms.transport_management_system.exception.InsufficientCapacityException;
import com.tms.transport_management_system.exception.InvalidStatusTransitionException;
import com.tms.transport_management_system.exception.LoadAlreadyBookedException;
import com.tms.transport_management_system.exception.ResourceNotFoundException;
import com.tms.transport_management_system.repository.BidRepository;
import com.tms.transport_management_system.repository.BookingRepository;
import com.tms.transport_management_system.repository.LoadRepository;
import com.tms.transport_management_system.repository.TransporterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Bid bid = bidRepository.findById(bookingDTO.getBidId()).orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bookingDTO.getBidId()));

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusTransitionException("Can only accept PENDING bids");
        }

        Load load = loadRepository.findById(bid.getLoadId()).orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + bid.getLoadId()));

        if (load.getStatus() == LoadStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Cannot book a canceled load");
        }

        Integer allocatedTrucks = bookingRepository.getTotalAllocatedTrucks(load.getLoadId(), BookingStatus.CONFIRMED);
        Integer remainingTrucks = load.getNoOfTrucks() - allocatedTrucks;

        if (remainingTrucks <= 0) {
            throw new LoadAlreadyBookedException("Load is already fully booked");
        }

        if (bid.getTrucksOffered() > remainingTrucks) {
            throw new InsufficientCapacityException(
                    "Only " + remainingTrucks + " trucks remaining, but bid offers " + bid.getTrucksOffered()
            );
        }

        Transporter transporter = transporterRepository.findById(bid.getTransporterId()).orElseThrow(() -> new ResourceNotFoundException("Transporter not found"));

        TruckCapacity truckCapacity = transporter.getAvailableTrucks().stream()
                .filter(tc -> tc.getTruckType().equals(load.getTruckType()))
                .findFirst()
                .orElseThrow(() -> new InsufficientCapacityException("Truck type not available"));

        if (truckCapacity.getCount() < bid.getTrucksOffered()) {
            throw new InsufficientCapacityException("Insufficient truck capacity");
        }

        truckCapacity.setCount(truckCapacity.getCount() - bid.getTrucksOffered());
        transporterRepository.save(transporter);

        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);

        Booking booking = new Booking();
        booking.setLoadId(load.getLoadId());
        booking.setBidId(bid.getBidId());
        booking.setTransporterId(bid.getTransporterId());
        booking.setAllocatedTrucks(bid.getTrucksOffered());
        booking.setFinalRate(bid.getProposedRate());
        booking.setStatus(BookingStatus.CONFIRMED);

        booking = bookingRepository.save(booking);

        Integer newAllocatedTrucks = allocatedTrucks + bid.getTrucksOffered();
        if (newAllocatedTrucks >= load.getNoOfTrucks()) {
            load.setStatus(LoadStatus.BOOKED);
            loadRepository.save(load);
        }

        return mapToDTO(booking);
    }

    public BookingDTO getBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        return mapToDTO(booking);
    }

    public BookingDTO cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidStatusTransitionException("Can only cancel CONFIRMED bookings");
        }

        Transporter transporter = transporterRepository.findById(booking.getTransporterId()).orElseThrow(() -> new ResourceNotFoundException("Transporter not found"));

        Load load = loadRepository.findById(booking.getLoadId()).orElseThrow(() -> new ResourceNotFoundException("Load not found"));

        TruckCapacity truckCapacity = transporter.getAvailableTrucks().stream()
                .filter(tc -> tc.getTruckType().equals(load.getTruckType()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Truck capacity not found"));

        truckCapacity.setCount(truckCapacity.getCount() + booking.getAllocatedTrucks());
        transporterRepository.save(transporter);

        booking.setStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);

        if (load.getStatus() == LoadStatus.BOOKED) {
            Integer confirmedTrucks = bookingRepository.getTotalAllocatedTrucks(load.getLoadId(), BookingStatus.CONFIRMED);
            if (confirmedTrucks < load.getNoOfTrucks()) {
                load.setStatus(LoadStatus.OPEN_FOR_BIDS);
                loadRepository.save(load);
            }
        }

        return mapToDTO(booking);
    }

    private BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setLoadID(booking.getLoadId());
        dto.setBidId(booking.getBidId());
        dto.setTransporterId(booking.getTransporterId());
        dto.setAllocatedTrucks(booking.getAllocatedTrucks());
        dto.setFinalRate(booking.getFinalRate());
        dto.setStatus(booking.getStatus());
        dto.setBookedAt(booking.getBookedAt());

        return dto;
    }

}
