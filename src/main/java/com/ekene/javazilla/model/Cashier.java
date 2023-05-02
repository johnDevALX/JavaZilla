package com.ekene.javazilla.model;

import com.ekene.javazilla.utility.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Entity
public class Cashier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String Sex;
    @Enumerated(EnumType.STRING)
    private Role role;
}
