package com.ekene.javazilla.utility;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddToCartDto {
    private Long productId;
    private Long customerId;
    private int desiredQty;
}
