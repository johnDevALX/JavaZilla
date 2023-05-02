package com.ekene.javazilla.service;

import com.ekene.javazilla.model.Cashier;
import com.ekene.javazilla.model.Manager;
import com.ekene.javazilla.model.Product;
import com.ekene.javazilla.repository.CashierRepository;
import com.ekene.javazilla.repository.ManagerRepository;
import com.ekene.javazilla.repository.ProductRepository;
import com.ekene.javazilla.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService{
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private CashierRepository cashierRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveManager(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public Boolean loginManager(LoginDto loginDto) {
        Optional<Manager> optional = managerRepository.findByEmail(loginDto.getEmail());
        Manager manager = null;
        if (optional.isPresent()){
            manager = optional.get();
            return manager.getPassWord().equalsIgnoreCase(loginDto.getPassWord());
        } else {
            throw new RuntimeException("Invalid Email Entered" + loginDto.getEmail());
        }
    }

    @Override
    public void hireCashier(Cashier cashier) {
        cashierRepository.save(cashier);
    }

    @Override
    public Boolean fireCashier(FireDto fireDto) {
        Optional<Cashier> optional = this.cashierRepository.findByEmail(fireDto.getEmail());
        Cashier cashier = null;
        if (optional.isPresent()){
            cashier = optional.get();
            if (Objects.equals(cashier.getId(), fireDto.getId())){
                cashierRepository.deleteById(cashier.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public void addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setImage(productDto.getImage());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setAvailabilityStatus(AvailabilityStatus.PRODUCT_IN_STOCK);
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public Product getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product product = null;
        if (optional.isPresent()){
            product = optional.get();
//            productDto.setId(product.getId());
//            productDto.setName(product.getName());
//            productDto.setPrice(product.getPrice());
//            productDto.setQuantity(product.getQuantity());
//            productDto.setAvailabilityStatus(product.getAvailabilityStatus());
        } else {
            throw new RuntimeException("######## Product Not Found ########");
        }
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(Long id, ProductDto productDto) {
        Product product = getProductById(id);
        product.setImage(productDto.getImage());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setAvailabilityStatus(AvailabilityStatus.PRODUCT_IN_STOCK);
        productRepository.save(product);
    }

    public Manager findManagerByEmail(String email){
       return managerRepository.findByEmail(email)
               .orElseThrow(() -> new IllegalArgumentException("Email: " + email + " not found"));
    }
}
