package com.app.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.app.config.AppConstants;
import com.app.entites.Product;
import com.app.payloads.CategoryDTO;
import com.app.payloads.ProductDTO;
import com.app.payloads.response.APIResponse;
import com.app.payloads.response.CategoryResponse;
import com.app.payloads.response.ProductResponse;
import com.app.services.CategoryService;
import com.app.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "8. Product Management")
@RestController
@RequestMapping("/categories")
@SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Operation(description="Create new category")
    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<APIResponse<?>> createCategory(@Valid @RequestBody CategoryDTO category) {
        var response = categoryService.createCategory(category);
        return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED.value(),response), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        CategoryResponse categoryResponse = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<APIResponse<?>> updateCategory(@RequestBody CategoryDTO category,
            @PathVariable Long categoryId) {
        var response = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(APIResponse.success(response), HttpStatus.OK);
    }


    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN'))")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }



    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {
        ProductDTO savedProduct = productService.addProduct(categoryId, product);
        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    //@PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('VENDOR'))")
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    //@PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or hasAuthority('VENDOR'))")
    @GetMapping("/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product, @PathVariable Long productId) {
        ProductDTO updatedProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    }


    @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
        String status = productService.deleteProduct(productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

    @GetMapping("/product-mapping")
    public ResponseEntity<APIResponse<?>> getCategoryProductMapping() {
        return new ResponseEntity<>(APIResponse.success(categoryService.getCategoryProductMapping()),HttpStatus.OK);
    }
}
