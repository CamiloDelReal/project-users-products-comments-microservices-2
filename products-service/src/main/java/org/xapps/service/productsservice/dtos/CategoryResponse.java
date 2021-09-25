package org.xapps.service.productsservice.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String name;
    @JsonBackReference
    private List<ProductResponse> products;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, List<ProductResponse> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }
}
