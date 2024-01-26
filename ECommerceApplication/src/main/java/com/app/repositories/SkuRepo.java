package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Sku;

@Repository
public interface SkuRepo extends JpaRepository<Sku, Long> {

}
