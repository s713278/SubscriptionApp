package com.app.repositories;

import com.app.entites.Sku;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuRepo extends JpaRepository<Sku, Long> {

    @Query("SELECT s FROM Sku s WHERE s.id = ?1 ")
    Optional<Sku> findByIdAndStoreId(Long skuId);
}
