package org.xapps.service.productsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xapps.service.productsservice.entities.CategoryProduct;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, CategoryProduct.CategoryProductId> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM categories_products WHERE product_id = :productId", nativeQuery = true)
    int deleteProductInCategories(@Param("productId") Long productId);

}
