package com.ekene.javazilla.service;

import com.ekene.javazilla.model.Cart;
import com.ekene.javazilla.model.Customer;
import com.ekene.javazilla.model.Product;
import com.ekene.javazilla.repository.CartRepository;
import com.ekene.javazilla.repository.CustomerRepository;
import com.ekene.javazilla.repository.ProductRepository;
import com.ekene.javazilla.utility.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService{

    // TODO Learn constructor dependency injection
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public void saveCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        customer.setBal(customerDto.getBal());
        customerRepository.save(customer);
    }

    @Override
    public Boolean customerLogin(LoginDto loginDto) {
        Optional<Customer> optional = customerRepository.findByEmail(loginDto.getEmail());
        Customer customer = null;
        if (optional.isPresent()){
            customer = optional.get();
            return customer.getPassword().equalsIgnoreCase(loginDto.getPassWord());
        } else {
            throw new RuntimeException("Invalid User Details Entered" + loginDto.getEmail());
        }
    }

    public Customer findCustomerByEmail(String email){
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email: " + email + " not found"));
    }

    @Override
    public List<Product> getProductByName(String keyword) {
        if (keyword != null){
            return productRepository.findByNameContainingIgnoreCase(keyword);
        } else
            return productRepository.findAll();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product product = null;
        ProductDto productDto = new ProductDto();
        if (optional.isPresent()){
            product = optional.get();
            productDto.setId(product.getId());
            productDto.setImage(product.getImage());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            productDto.setQuantity(product.getQuantity());
            productDto.setAvailabilityStatus(product.getAvailabilityStatus());
        } else {
            throw new RuntimeException("######## Product Not Found ########");
        }
        return productDto;
    }

    @Override
    public void addToCart(AddToCartDto addToCartDto) {
        Product product = productRepository.findById(addToCartDto.getProductId()).get();
        Customer customer = customerRepository.findById(addToCartDto.getCustomerId()).get();
        Optional<Cart> checkDupCart = cartRepository.findByProduct_IdAndCustomer_Id(addToCartDto.getProductId(), addToCartDto.getCustomerId());
        if (product.getQuantity() >= addToCartDto.getDesiredQty()){
            Cart cart;
            if (checkDupCart.isPresent()){
                cart = checkDupCart.get();
                cart.setDesiredQty(cart.getDesiredQty() + addToCartDto.getDesiredQty());
            } else {
                cart = new Cart();
//        cart.setProduct(Collections.singletonList(product));
                cart.setProduct(product);
                cart.setCustomer(customer);
                cart.setDesiredQty(addToCartDto.getDesiredQty());
            }
            cartRepository.save(cart);
        }
    }

    @Override
    public List<ShowProductsInCartDto> findAllCartByCustomer(Long customerId) {

        List<Cart> cartByCustomer_id = cartRepository.findCartByCustomer_Id(customerId);
        cartByCustomer_id.forEach(System.out::println);
//        System.out.println("from cart###################################");
        List<ShowProductsInCartDto> showProductsInCartDtoList = new ArrayList<>();
        for (Cart cart:cartByCustomer_id) {
            BigDecimal total = new BigDecimal(0);
            ShowProductsInCartDto showProductsInCartDto = new ShowProductsInCartDto();
            Product product =  productRepository.findById(cart.getProduct().getId()).get();
//            log.debug("Product  ##############################---->: {}", product);
            showProductsInCartDto.setId(cart.getId());
            showProductsInCartDto.setProductImage(product.getImage());
            showProductsInCartDto.setProductName(product.getName());
            showProductsInCartDto.setProductPrice(product.getPrice());
            showProductsInCartDto.setDesiredQty(cart.getDesiredQty());
            total = (product.getPrice().multiply(BigDecimal.valueOf(cart.getDesiredQty())));
            log.debug("Products In Carts ===============>: {}", showProductsInCartDto);
            showProductsInCartDto.setTotal(total);
            showProductsInCartDtoList.add(showProductsInCartDto);
            System.out.println("Carts total: " + total);
        }
//        System.out.println("from cartDTO#############################");
//        showProductsInCartDtoList.forEach(System.out::println);
//        double newTotal = 0.0;
//        for (ShowProductsInCartDto showProductsInCartDto:
//             showProductsInCartDtoList) {
//            BigDecimal tt = new BigDecimal("0");
//            System.out.println(showProductsInCartDto.getTotal());
//            tt = (showProductsInCartDto.getTotal());
//            newTotal += tt.doubleValue();
//        }
//        System.out.println(newTotal + " ###################END");
        return showProductsInCartDtoList;
    }

    @Override
    public Boolean sellCartProducts(Long customerId) {
        Customer customer = customerRepository.findById(customerId).get();
        double cartTotal = checkCartTotal(customerId);
        if (customer.getBal().compareTo(BigDecimal.valueOf(cartTotal)) >= 1){
            customer.setBal(customer.getBal().subtract(BigDecimal.valueOf(cartTotal)));
            List<Cart> cartByCustomer_id = cartRepository.findCartByCustomer_Id(customerId);
            for (Cart cart:cartByCustomer_id) {
                Product product = cart.getProduct();
                Product product1 = productRepository.findById(product.getId()).get();
                product1.setQuantity(product1.getQuantity() - cart.getDesiredQty());
                productRepository.save(product1);
            }
            cartRepository.deleteAllByCustomer_Id(customerId);
            return true;
        } else
            return false;
    }

    @Override
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    private double checkCartTotal(Long CustomerId){
        double newTotal = 0.0;
        for (ShowProductsInCartDto showProductsInCartDto:
                findAllCartByCustomer(CustomerId)) {
            BigDecimal tt = new BigDecimal("0");
            System.out.println(showProductsInCartDto.getTotal());
            tt = (showProductsInCartDto.getTotal());
            newTotal += tt.doubleValue();
        }
        return newTotal;
    }
}
