package com.app.services;

import com.app.entites.Product;
import com.app.payloads.ProductDTO;
import com.app.payloads.response.PaginationResponse;
import com.app.repositories.projections.ProductProjection;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

  ProductDTO addProduct(Long categoryId, Product product);

  PaginationResponse<?> getAllProducts(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

  ProductDTO updateProduct(Long productId, Product product);

  ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

  PaginationResponse<?> searchProductByKeyword(
      String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

  String deleteProduct(Long productId);

  List<ProductProjection> fetchProductsByCategory(Long id);
}
