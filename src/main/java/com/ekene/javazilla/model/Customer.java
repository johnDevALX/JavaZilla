package com.ekene.javazilla.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal bal;
}
