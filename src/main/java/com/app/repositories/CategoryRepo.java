package com.app.repositories;

import com.app.entites.Category;
import com.app.repositories.projections.CategoryProjection;
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
}
