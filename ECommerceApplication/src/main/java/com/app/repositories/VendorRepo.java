package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Vendor;

public interface VendorRepo extends JpaRepository<Vendor, Long> {
}
