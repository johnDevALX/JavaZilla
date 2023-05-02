package com.ekene.javazilla.model;

import com.ekene.javazilla.utility.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    @Column(unique = true)
    private String name;

    private BigDecimal price;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
}
