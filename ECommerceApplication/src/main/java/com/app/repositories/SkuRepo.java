package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Sku;

@Repository
public interface SkuRepo extends JpaRepository<Sku, Long> {
	
	@Query("SELECT s FROM Sku s WHERE s.id = ?1 and s.store.id=?2")
	Optional<Sku> findByIdAndStoreId(Long skuId,Long storeId);

}
