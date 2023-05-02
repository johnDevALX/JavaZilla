package com.ekene.javazilla.repository;

import com.ekene.javazilla.model.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    Optional<Cashier> findByEmail(String email);
}
