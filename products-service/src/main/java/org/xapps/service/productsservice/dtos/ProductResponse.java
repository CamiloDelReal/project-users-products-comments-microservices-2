package org.xapps.service.productsservice.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    @JsonManagedReference
    private List<CategoryResponse> categories;
    private List<CommentResponse> comments;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description, List<CategoryResponse> categories, List<CommentResponse> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.comments = comments;
    }
}
