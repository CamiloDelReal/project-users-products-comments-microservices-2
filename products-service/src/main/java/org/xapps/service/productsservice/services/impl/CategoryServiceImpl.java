package org.xapps.service.productsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.service.productsservice.dtos.CategoryRequest;
import org.xapps.service.productsservice.dtos.CategoryResponse;
import org.xapps.service.productsservice.entities.Category;
import org.xapps.service.productsservice.repositories.CategoryRepository;
import org.xapps.service.productsservice.services.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> response;
        List<Category> categories = categoryRepository.findAll();
        if(!categories.isEmpty()) {
            response = categories.stream().map(it -> modelMapper.map(it, CategoryResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        CategoryResponse response = null;
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) {
            response = modelMapper.map(category, CategoryResponse.class);
        }
        return response;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = modelMapper.map(categoryRequest, Category.class);
        categoryRepository.save(category);
        CategoryResponse response = modelMapper.map(category, CategoryResponse.class);
        return response;
    }

    @Override
    public CategoryResponse editCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id).orElse(null);
        CategoryResponse response = null;
        if(category != null) {
            category.setName(categoryRequest.getName());
            categoryRepository.save(category);
            response = modelMapper.map(category, CategoryResponse.class);
        }
        return response;
    }

    @Override
    public boolean deleteCategory(Long id) {
        boolean success = false;
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null) {
            categoryRepository.delete(category);
            success = true;
        }
        return success;
    }

}
