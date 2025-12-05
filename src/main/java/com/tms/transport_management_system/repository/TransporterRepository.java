package com.tms.transport_management_system.repository;

import com.tms.transport_management_system.entity.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransporterRepository extends JpaRepository<Transporter, UUID> {
}
