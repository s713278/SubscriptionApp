package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entites.Vendor;
import com.app.entites.type.VendorStatus;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, Long> {
    List<Vendor> findAllByStatus(VendorStatus status);

    @Query(value = """
            SELECT * 
            FROM tb_vendor 
            WHERE EXISTS (
                SELECT 1 
                FROM jsonb_array_elements_text(service_area->'areas') AS area
                WHERE area LIKE %:substring%
            )
            """, nativeQuery = true)
    List<Vendor> findByServiceArea(@Param("substring") String substring);
}
