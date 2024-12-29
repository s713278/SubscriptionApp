package com.app.repositories;

import com.app.entites.Product;
import com.app.repositories.projections.ProductProjection;
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
public interface ProductRepo extends JpaRepository<Product, Long> {

  Page<Product> findByNameLike(String keyword, Pageable pageDetails);

  @Query(
      "SELECT p.id as id, p.name as name, p.imagePath as imagePath FROM Product p WHERE p.category.id=:categoryId")
  List<ProductProjection> findProductsByCategory(final Long categoryId);

  @Query("SELECT p.id FROM Product p WHERE p.category.id =:categoryId AND p.id IN :ids")
  Optional<Set<Long>> findValidProductIDs(
      @Param("categoryId") Long categoryId, @Param("ids") Set<Long> ids);
}
