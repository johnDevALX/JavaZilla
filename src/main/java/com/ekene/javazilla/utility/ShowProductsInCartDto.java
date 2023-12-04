package com.ekene.javazilla.utility;

import lombok.*;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@ToString
public class ShowProductsInCartDto {
    private Long id;
    private String productImage;
    private String productName;
    private BigDecimal productPrice;
    private int desiredQty;
    private BigDecimal total;
}
