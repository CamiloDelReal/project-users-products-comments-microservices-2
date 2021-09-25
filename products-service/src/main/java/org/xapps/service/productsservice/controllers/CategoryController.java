package org.xapps.service.productsservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.service.productsservice.dtos.CategoryRequest;
import org.xapps.service.productsservice.dtos.CategoryResponse;
import org.xapps.service.productsservice.services.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        logger.info("getAllCategories");
        ResponseEntity<List<CategoryResponse>> response = null;
        try {
            List<CategoryResponse> categories = categoryService.getAllCategories();
            response = new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Internal error captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id) {
        ResponseEntity<CategoryResponse> response = null;
        try {
            CategoryResponse category = categoryService.getCategoryById(id);
            if(category != null) {
                response = new ResponseEntity<>(category, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Internal error captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        ResponseEntity<CategoryResponse> response = null;
        try {
            CategoryResponse category = categoryService.createCategory(categoryRequest);
            response = new ResponseEntity<>(category, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.error("Internar error captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<CategoryResponse> editCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        ResponseEntity<CategoryResponse> response = null;
        try {
            CategoryResponse category = categoryService.editCategory(id, categoryRequest);
            if(category != null) {
                response = new ResponseEntity<>(category, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Internal error captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        ResponseEntity<Void> response = null;
        try {
            boolean success = categoryService.deleteCategory(id);
            if(success) {
                response = new ResponseEntity<>(HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Internal error captured", ex);
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
