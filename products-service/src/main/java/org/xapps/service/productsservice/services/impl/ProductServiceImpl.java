package org.xapps.service.productsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.service.productsservice.dtos.ProductRequest;
import org.xapps.service.productsservice.dtos.ProductResponse;
import org.xapps.service.productsservice.entities.CategoryProduct;
import org.xapps.service.productsservice.entities.Product;
import org.xapps.service.productsservice.repositories.CategoryProductRepository;
import org.xapps.service.productsservice.repositories.ProductRepository;
import org.xapps.service.productsservice.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl  implements ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private ModelMapper modelMapper;
    private ProductRepository productRepository;
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository, CategoryProductRepository categoryProductRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.categoryProductRepository = categoryProductRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response;
        if(!products.isEmpty()) {
            response = products.stream().map(it -> modelMapper.map(it, ProductResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        ProductResponse response = null;
        if(product != null) {
            response = modelMapper.map(product, ProductResponse.class);
        }
        return response;
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product d = productRepository.save(product);
        final Long productId = product.getId();
        List<CategoryProduct> categories = productRequest.getCategories().stream().map(categoryId -> new CategoryProduct(categoryId, productId)).collect(Collectors.toList());
        categoryProductRepository.saveAll(categories);
        product = productRepository.findById(productId).orElse(null);  // Shoudn't return null
        product.getCategories().forEach(System.out::println);
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        return response;
    }

    @Override
    public ProductResponse editProduct(Long id, ProductRequest productRequest) {
        ProductResponse response = null;
        Product product = productRepository.findById(id).orElse(null);
        if(product != null) {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            productRepository.save(product);
            final Long productId = product.getId();
            categoryProductRepository.deleteProductInCategories(productId);
            List<CategoryProduct> categories = productRequest.getCategories().stream().map(categoryId -> new CategoryProduct(categoryId, productId)).collect(Collectors.toList());
            categoryProductRepository.saveAll(categories);
            product = productRepository.findById(id).orElse(null);  // Shoudn't return null
            response = modelMapper.map(product, ProductResponse.class);
        }
        return response;
    }

    @Override
    public boolean deleteProduct(Long id) {
        boolean success = false;
        Product product = productRepository.findById(id).orElse(null);
        if(product != null) {
            productRepository.delete(product);
            success = true;
        }
        return success;
    }
}
