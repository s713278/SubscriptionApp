package com.app.services;

import com.app.payloads.CategoryDTO;
import com.app.payloads.response.CategoryResponse;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO category);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);

    String deleteCategory(Long categoryId);
}
