package com.app.services;

import java.util.List;

import com.app.payloads.CategoryDTO;
import com.app.payloads.CategoryWithProductsDTO;
import com.app.payloads.response.CategoryResponse;
import com.app.payloads.response.CreateItemResponse;

public interface CategoryService {

    CreateItemResponse createCategory(CategoryDTO category);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CreateItemResponse updateCategory(CategoryDTO category, Long categoryId);

    String deleteCategory(Long categoryId);
    List<CategoryWithProductsDTO> getCategoryProductMapping();
}
