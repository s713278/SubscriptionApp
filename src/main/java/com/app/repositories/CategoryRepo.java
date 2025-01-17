package com.app.repositories;

import com.app.entites.Category;
import com.app.repositories.projections.CategoryProjection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

  Category findByNameIgnoreCase(final String name);

  @Query("SELECT c.id as id, c.name as name, c.imagePath as imagePath FROM Category c")
  Page<CategoryProjection> findAllCategories(Pageable pageable);

  @Query("SELECT c.id FROM Category c WHERE c.id IN :ids")
  Optional<Set<Long>> findValidCategories(@Param("ids") Set<Long> ids);

  @Query(
      value =
          """
          SELECT
                 -- tv.id AS vendorId,
                 -- tv.business_name AS businessName,
                  DISTINCT
                  tcv.category_id AS id,
                  tc.name AS name,
                  tc.image_path  as imagePath
              FROM
                  tb_vendor tv
              JOIN
                  tb_category_vendor tcv
                  ON tv.id = tcv.vendor_id
              JOIN
                  tb_category tc
                  ON tcv.category_id = tc.id
              WHERE
                  EXISTS (
                      SELECT 1
                      FROM jsonb_array_elements_text(tv.service_area->'areas') area
                      WHERE area LIKE CONCAT('%', :serviceArea, '%')
                  )
              AND
                  EXISTS (
                      SELECT 1
                      FROM tb_product tp
                      WHERE tp.category_id = tcv.category_id
                  );
          """,
      nativeQuery = true)
  List<CategoryProjection> findCategoriesByServiceArea(String serviceArea);
}
