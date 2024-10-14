package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Vendor;
import com.app.entites.type.VendorStatus;

public interface VendorRepo extends JpaRepository<Vendor, Long> {
    List<Vendor> findAllByStatus(VendorStatus status);
}
