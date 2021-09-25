package org.xapps.service.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ProductRequest {
    @NotNull(message = "Name cannot be empty")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;

    private String description;

    private List<Long> categories;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProductRequest(String name, String description, List<Long> categories) {
        this.name = name;
        this.description = description;
        this.categories = categories;
    }
}
