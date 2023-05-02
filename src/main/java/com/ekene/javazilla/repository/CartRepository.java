package com.ekene.javazilla.repository;

import com.ekene.javazilla.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByProduct_IdAndCustomer_Id(Long product_id, Long customerId);
    List<Cart> findCartByCustomer_Id(Long customerId);
}
