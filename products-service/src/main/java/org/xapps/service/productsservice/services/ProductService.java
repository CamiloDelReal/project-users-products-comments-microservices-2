package org.xapps.service.productsservice.services;

import org.xapps.service.productsservice.dtos.ProductRequest;
import org.xapps.service.productsservice.dtos.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse editProduct(Long id, ProductRequest productRequest);

    boolean deleteProduct(Long id);

}
