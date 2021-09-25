package org.xapps.service.productsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xapps.service.productsservice.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
