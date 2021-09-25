package org.xapps.service.productsservice.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.service.productsservice.dtos.CommentResponse;
import org.xapps.service.productsservice.dtos.ProductRequest;
import org.xapps.service.productsservice.dtos.ProductResponse;
import org.xapps.service.productsservice.services.CommentService;
import org.xapps.service.productsservice.services.ProductService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    private ProductService productService;
    private CommentService commentService;

    @Autowired
    public ProductController(ProductService productService, CommentService commentService) {
        this.productService = productService;
        this.commentService = commentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        ResponseEntity<List<ProductResponse>> response = null;
        try {
            List<ProductResponse> productResponse = productService.getAllProducts();
            response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CircuitBreaker(name = "commentsByProductBreaker", fallbackMethod = "getProductByIdFallback")
    @Retry(name = "commentsByProductRetry", fallbackMethod = "getProductByIdFallback")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        ResponseEntity<ProductResponse> response = null;
        ProductResponse product = productService.getProductById(id);
        if (product != null) {
            List<CommentResponse> comments = commentService.getCommentsByProduct(id);
            product.setComments(comments);
            response = new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    public ResponseEntity<ProductResponse> getProductByIdFallback(Long id, Throwable t) {
        logger.info("Calling fallback for getProductById");
        ResponseEntity<ProductResponse> response = null;
        try {
            ProductResponse product = productService.getProductById(id);
            if (product != null) {
                response = new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ResponseEntity<ProductResponse> response = null;
        try {
            ProductResponse product = productService.createProduct(productRequest);

            response = new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<ProductResponse> editProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest productRequest) {
        ResponseEntity<ProductResponse> response = null;
        try {
            ProductResponse product = productService.editProduct(id, productRequest);
            if(product != null) {
                response = new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        ResponseEntity response = null;
        try {
            boolean success = productService.deleteProduct(id);
            if(success) {
                response = new ResponseEntity(HttpStatus.OK);
            } else {
                response = new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Exception captured", ex);
            response = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
