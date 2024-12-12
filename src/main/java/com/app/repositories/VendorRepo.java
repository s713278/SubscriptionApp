package com.app.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
        SELECT 
            tv.id AS vendor_id, 
            tv.business_name, 
            tv.banner_image, 
            tv.service_area, 
            ARRAY_AGG(DISTINCT tc.name) AS categories
        FROM 
            tb_vendor tv
        JOIN 
            tb_category tc 
        ON 
            tv.id = tc.vendor_id
        WHERE 
            tv.status = 'ACTIVE'
        GROUP BY 
            tv.id, tv.business_name, tv.banner_image, tv.service_area;
        """, nativeQuery = true)
    List<Object[]> findAllUniqueVendorsWithCategories();

    @Query(value = """
            SELECT
              tv.id AS vendor_id,
              tv.business_name,
              tv.banner_image,
              tv.service_area,
              ARRAY_AGG(DISTINCT tc.name) AS categories
          FROM
              tb_vendor tv
          JOIN
              tb_category tc
          ON
              tv.id = tc.vendor_id
          WHERE
              tv.status = 'ACTIVE'
              AND EXISTS (
                  SELECT 1
                  FROM jsonb_array_elements_text(tv.service_area->'areas') AS area
                  WHERE area LIKE CONCAT('%', :zipCode, '%')
              )
          GROUP BY
              tv.id, tv.business_name, tv.banner_image, tv.service_area;
        """, nativeQuery = true)
    List<Object[]> findAllUniqueVendorsWithCategories(String zipCode);

    @Query(value = """
            SELECT
              tv.id AS vendor_id,
              tv.business_name,
              tv.banner_image,
              tv.service_area,
              ARRAY_AGG(DISTINCT tc.name) AS categories
          FROM
              tb_vendor tv
          JOIN
              tb_category tc
          ON
              tv.id = tc.vendor_id
          WHERE
              tv.status = 'ACTIVE'
              AND EXISTS (
                  SELECT 1
                  FROM jsonb_array_elements_text(tv.service_area->'areas') AS area
                  WHERE area LIKE CONCAT('%', :zipCode, '%')
              )
          GROUP BY
              tv.id, tv.business_name, tv.banner_image, tv.service_area;
        """, nativeQuery = true)
    //List<Object[]> findAllUniqueVendorsWithCategories(String zipCode, Pageable pageable);
    Page<Object[]> findActiveVendorsByZipCode(String zipCode, Pageable pageable);

}
