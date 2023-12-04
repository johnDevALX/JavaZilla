package com.ekene.javazilla.service;

import com.ekene.javazilla.model.Cart;
import com.ekene.javazilla.model.Customer;
import com.ekene.javazilla.model.Product;
import com.ekene.javazilla.utility.*;

import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDto customerDto);
    Boolean customerLogin(LoginDto loginDto);
    List<Product> getProductByName(String keyword);
    ProductDto getProductById(Long id);
    void addToCart(AddToCartDto addToCartDto);
    List<ShowProductsInCartDto> findAllCartByCustomer(Long customerId);
    Boolean sellCartProducts(Long customerId);
    void deleteCart(Long cartId);
}
