package com.ekene.javazilla.repository;

import com.ekene.javazilla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query("SELECT product FROM Product product WHERE product.name LIKE %?1%")
    List<Product> findByNameContainingIgnoreCase(@Param(value = "keyword") String keyword);
}
