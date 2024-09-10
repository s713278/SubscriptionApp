package com.app.repositories;

import com.app.entites.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepo extends JpaRepository<Vendor, Long> {
}
