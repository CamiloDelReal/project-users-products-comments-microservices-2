package org.xapps.service.productsservice.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "categories_products")
public class CategoryProduct {

    @EmbeddedId
    private CategoryProductId id;

    public CategoryProduct() {
    }

    public CategoryProduct(Long categoryId, Long productId) {
        this.id = new CategoryProductId(categoryId, productId);
    }

    @Data
    @Embeddable
    public static class CategoryProductId implements Serializable {
        @Column(name = "category_id")
        private Long categoryId;

        @Column(name = "product_id")
        private Long productId;

        public CategoryProductId() {
        }

        public CategoryProductId(Long categoryId, Long productId) {
            this.categoryId = categoryId;
            this.productId = productId;
        }
    }
}
