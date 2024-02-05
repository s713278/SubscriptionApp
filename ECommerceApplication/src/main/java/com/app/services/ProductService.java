package com.app.services;

import com.app.entites.Product;
import com.app.payloads.ProductDTO;
import com.app.payloads.response.ProductResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(
            Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, Product product);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse searchProductByKeyword(
            String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    String deleteProduct(Long productId);
}
