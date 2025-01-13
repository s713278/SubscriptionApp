package com.app.services;

import com.app.payloads.CategoryDTO;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.PaginationResponse;
import com.app.repositories.projections.CategoryProjection;
import com.app.repositories.projections.ProductProjection;
import java.util.List;
import java.util.Map;

public interface CategoryService {

  CreateItemResponse createCategory(CategoryDTO category);

  PaginationResponse<?> getCategories(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

  CreateItemResponse updateCategory(CategoryDTO category, Long categoryId);

  String deleteCategory(Long categoryId);

  List<ProductProjection> fetchProductsByCategory(Long categoryId);

  Map<String, List<CategoryDTO>> fetchCategoriesByType();

  List<CategoryProjection> fetchCategoriesByServiceArea(String serviceArea);
}
