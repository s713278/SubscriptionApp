package com.app.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Category;
import com.app.entites.Product;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CategoryDTO;
import com.app.payloads.CategoryWithProductsDTO;
import com.app.payloads.response.CreateItemResponse;
import com.app.payloads.response.PaginationResponse;
import com.app.repositories.CategoryRepo;
import com.app.services.CategoryService;
import com.app.services.ProductService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CreateItemResponse createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepo.findByNameIgnoreCase(category.getName());
        if (savedCategory != null) {
            throw new APIException(APIErrorCode.DUPLICATE_REQUEST,
                    "Category with the name '" + category.getName() + "' already exists !!!");
        }
        savedCategory = categoryRepo.save(category);
        return new CreateItemResponse(savedCategory.getId(),"Category created successfully");
    }

    @Override
    public PaginationResponse<?> getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> pageCategories = categoryRepo.findAll(pageDetails);

        List<Category> categories = pageCategories.getContent();

        if (categories.isEmpty()) {
            throw new APIException(APIErrorCode.API_400,"No category is created till now");
        }

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
        return new PaginationResponse<>(categoryDTOs,pageCategories.getNumber(),
                pageCategories.getSize(),pageCategories.getTotalElements(),pageCategories.getTotalPages(),pageCategories.isLast());
    }

    @Override
    public CreateItemResponse updateCategory(CategoryDTO category, Long categoryId) {
        Category savedCategory  = categoryRepo.save(modelMapper.map(category, Category.class));
        return new CreateItemResponse(savedCategory.getId(),"Category updated successfully");
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> products = null;//category.getProducts();

        products.forEach(product -> {
            productService.deleteProduct(product.getId());
        });

        categoryRepo.delete(category);

        return "Category with categoryId: " + categoryId + " deleted successfully !!!";
    }

    public List<CategoryWithProductsDTO> getCategoryProductMapping() {
        return categoryRepo.findAll().stream()
                .map(category -> new CategoryWithProductsDTO(
                        category.getId(),
                        category.getName(),
                        category.getProducts().stream()
                                .map(product -> new CategoryWithProductsDTO.Data(product.getId(), product.getName()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
