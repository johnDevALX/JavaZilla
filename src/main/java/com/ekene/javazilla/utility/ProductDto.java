package com.ekene.javazilla.utility;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String image;
    private String name;
    private BigDecimal price;
    private int quantity;
    private AvailabilityStatus availabilityStatus;
}
