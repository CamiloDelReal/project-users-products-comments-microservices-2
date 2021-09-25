package org.xapps.service.productsservice.services;

import org.xapps.service.productsservice.dtos.CategoryRequest;
import org.xapps.service.productsservice.dtos.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse editCategory(Long id, CategoryRequest categoryRequest);

    boolean deleteCategory(Long id);

}
