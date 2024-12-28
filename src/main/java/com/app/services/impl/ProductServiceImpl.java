package com.app.services.impl;

import com.app.config.GlobalConfig;
import com.app.constants.CacheType;
import com.app.entites.Cart;
import com.app.entites.Category;
import com.app.entites.Product;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CartDTO;
import com.app.payloads.ProductDTO;
import com.app.payloads.response.PaginationResponse;
import com.app.repositories.CartRepo;
import com.app.repositories.RepositoryManager;
import com.app.repositories.projections.ProductProjection;
import com.app.services.CartService;
import com.app.services.FileService;
import com.app.services.ProductService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

  @Autowired private RepositoryManager repoManager;

  @Autowired private CartRepo cartRepo;

  @Autowired private CartService cartService;

  @Autowired private FileService fileService;

  @Autowired private ModelMapper modelMapper;

  private GlobalConfig globalConfig;

  @Override
  public ProductDTO addProduct(Long categoryId, Product product) {

    Category category =
        repoManager
            .getCategoryRepo()
            .findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

    boolean isProductNotPresent = true;

    List<Product> products = Collections.emptyList(); // category.getProducts();

    for (int i = 0; i < products.size(); i++) {
      if (products.get(i).getName().equals(product.getName())
          && products.get(i).getDescription().equals(product.getDescription())) {

        isProductNotPresent = false;
        break;
      }
    }
    if (isProductNotPresent) {
      product.setImagePath("default.png");
      product.setCategory(category);
      Product savedProduct = repoManager.getProductRepo().save(product);
      return modelMapper.map(savedProduct, ProductDTO.class);
    } else {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Product already exists !!!");
    }
  }

  @Override
  public PaginationResponse<?> getAllProducts(
      Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder =
        sortOrder.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Product> pageProducts = repoManager.getProductRepo().findAll(pageDetails);
    List<Product> products = pageProducts.getContent();
    List<ProductDTO> productDTOs =
        products.stream()
            .map(product -> modelMapper.map(product, ProductDTO.class))
            .collect(Collectors.toList());
    return new PaginationResponse<>(
        productDTOs,
        pageProducts.getNumber(),
        pageProducts.getSize(),
        pageProducts.getTotalElements(),
        pageProducts.getTotalPages(),
        pageProducts.isLast());
  }

  @Override
  public PaginationResponse<?> searchProductByKeyword(
      String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder =
        sortOrder.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Product> pageProducts = repoManager.getProductRepo().findByNameLike(keyword, pageDetails);
    List<Product> products = pageProducts.getContent();
    if (products.isEmpty()) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED, "Products not found with keyword: " + keyword);
    }
    List<ProductDTO> productDTOs =
        products.stream()
            .map(p -> modelMapper.map(p, ProductDTO.class))
            .collect(Collectors.toList());

    return new PaginationResponse<>(
        productDTOs,
        pageProducts.getNumber(),
        pageProducts.getSize(),
        pageProducts.getTotalElements(),
        pageProducts.getTotalPages(),
        pageProducts.isLast());
  }

  @Override
  public ProductDTO updateProduct(Long productId, Product product) {
    Product productFromDB =
        repoManager
            .getProductRepo()
            .findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
    if (productFromDB == null) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED, "Product not found with productId: " + productId);
    }
    product.setImagePath(productFromDB.getImagePath());
    product.setId(productId);
    product.setCategory(productFromDB.getCategory());
    Product savedProduct = repoManager.getProductRepo().save(product);
    List<Cart> carts = cartRepo.findCartsBySkuId(productId);
    List<CartDTO> cartDTOs =
        carts.stream()
            .map(
                cart -> {
                  CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                  return cartDTO;
                })
            .toList();
    cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));
    return modelMapper.map(savedProduct, ProductDTO.class);
  }

  @Override
  public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
    Product productFromDB =
        repoManager
            .getProductRepo()
            .findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

    if (productFromDB == null) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED, "Product not found with productId: " + productId);
    }
    String fileName = fileService.uploadImage(globalConfig.getImagePath(), image);
    productFromDB.setImagePath(fileName);
    Product updatedProduct = repoManager.getProductRepo().save(productFromDB);
    return modelMapper.map(updatedProduct, ProductDTO.class);
  }

  @Override
  public String deleteProduct(Long productId) {
    repoManager.getProductRepo().deleteById(productId);
    return "Product with productId: " + productId + " deleted successfully !!!";
  }

  @Cacheable(value = CacheType.CACHE_TYPE_PRODUCTS, key = "#categoryId")
  @Override
  public List<ProductProjection> fetchProductsByCategory(Long categoryId) {
    return repoManager.getProductRepo().findProductsByCategory(categoryId);
  }
}
