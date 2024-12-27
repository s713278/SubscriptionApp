package com.app.controllers;

import com.app.config.AppConstants;
import com.app.payloads.ProductDTO;
import com.app.payloads.response.APIResponse;
import com.app.services.CategoryService;
import com.app.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "8. Product Catalog API", description = "APIs for managing category,product and skus")
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

  @Autowired private CategoryService categoryService;
  @Autowired private ProductService productService;

  @Operation(
      summary = "Fetch All Categories",
      description = "API accessed by  Admin/Customer_Care roles")
  @GetMapping("/")
  public ResponseEntity<APIResponse<?>> getCategories(
      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)
          Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)
          Integer pageSize,
      @RequestParam(
              name = "sortBy",
              defaultValue = AppConstants.SORT_CATEGORIES_BY,
              required = false)
          String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false)
          String sortOrder) {
    var categoryResponse = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
    return new ResponseEntity<>(APIResponse.success(categoryResponse), HttpStatus.OK);
  }

  /*

      @SecurityRequirement(name = AppConstants.SECURITY_CONTEXT_PARAM)
   @Operation(summary = "Create a new category", description="API accessed by only Admin roles")
   @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
   @PostMapping("/")
   public ResponseEntity<APIResponse<?>> createCategory(@Valid @RequestBody CategoryDTO category) {
       var response = categoryService.createCategory(category);
       return new ResponseEntity<>(APIResponse.success(HttpStatus.CREATED.value(),response), HttpStatus.CREATED);
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


   @Operation(summary = "Fetch Products", description="API accessed by  Admin/Customer_Care roles")
  // @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
   @GetMapping("/products")
   public ResponseEntity<APIResponse<?>> getAllProducts(
           @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
           @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
           @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
           @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
       var productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
       return new ResponseEntity<>(APIResponse.success(productResponse), HttpStatus.FOUND);
   }
    */

  @Operation(
      summary = "Fetch products by category",
      description = "API accessed by Admin/Customer_Care role only")
  // @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN') or
  // hasAuthority('VENDOR'))")
  @GetMapping("/{categoryId}/products")
  public ResponseEntity<APIResponse<?>> getProductsByCategory(@PathVariable Long categoryId) {
    var products = categoryService.fetchProductsByCategory(categoryId);
    return new ResponseEntity<>(APIResponse.success(products), HttpStatus.FOUND);
  }

  @Operation(
      summary = "Search product with keyword",
      description = "API accessed by Admin/Customer_Care role only")
  @PreAuthorize("(hasAuthority('ADMIN') OR hasAuthority('CUSTOMER_CARE')")
  @GetMapping("/products/keyword/{keyword}")
  public ResponseEntity<APIResponse<?>> getProductsByKeyword(
      @PathVariable String keyword,
      @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false)
          Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)
          Integer pageSize,
      @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false)
          String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false)
          String sortOrder) {
    var productResponse =
        productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
    return new ResponseEntity<>(APIResponse.success(productResponse), HttpStatus.FOUND);
  }

  @Operation(
      summary = "Image Upload",
      description = "API accessed by Admin/Customer_Care role only")
  @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
  @PutMapping("/products/{productId}/image")
  public ResponseEntity<APIResponse<?>> updateProductImage(
      @PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
    ProductDTO updatedProduct = productService.updateProductImage(productId, image);
    return new ResponseEntity<>(APIResponse.success("Image uploaded successfully."), HttpStatus.OK);
  }

  /*

     @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
     @PutMapping("/products/{productId}")
     public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product, @PathVariable Long productId) {
         ProductDTO updatedProduct = productService.updateProduct(productId, product);
         return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
     }
     @PreAuthorize("#userId == authentication.principal and (hasAuthority('ADMIN')")
     @DeleteMapping("/products/{productId}")
     public ResponseEntity<String> deleteProductByCategory(@PathVariable Long productId) {
         String status = productService.deleteProduct(productId);

         return new ResponseEntity<String>(status, HttpStatus.OK);
     }
  */

}
