package com.ekene.javazilla.service;

import com.ekene.javazilla.model.Cashier;
import com.ekene.javazilla.model.Manager;
import com.ekene.javazilla.model.Product;
import com.ekene.javazilla.utility.ApplicantDto;
import com.ekene.javazilla.utility.FireDto;
import com.ekene.javazilla.utility.LoginDto;
import com.ekene.javazilla.utility.ProductDto;

import java.util.List;

public interface ManagerService {
    void saveManager(Manager manager);
    Boolean loginManager(LoginDto loginDto);
    void hireCashier(Cashier cashier);
    Boolean fireCashier(FireDto fireDto);
    void addProduct(ProductDto productDto);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProduct(Long id);
    void updateProduct(Long id, ProductDto productDto);

}
