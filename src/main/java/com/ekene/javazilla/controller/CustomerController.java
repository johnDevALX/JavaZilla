package com.ekene.javazilla.controller;

import com.ekene.javazilla.model.Customer;
import com.ekene.javazilla.model.Product;
import com.ekene.javazilla.service.CustomerService;
import com.ekene.javazilla.service.CustomerServiceImpl;
import com.ekene.javazilla.utility.AddToCartDto;
import com.ekene.javazilla.utility.CustomerDto;
import com.ekene.javazilla.utility.LoginDto;
import com.ekene.javazilla.utility.ProductDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class CustomerController {




    private CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService1){
        this.customerService = customerService1;
    }

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @GetMapping("/customerAcctForm")
    public String customerAcctForm(Model model){
        CustomerDto customerDto = new CustomerDto();
        model.addAttribute("customerDto", customerDto);
        return "customer_setup";
    }

    @PostMapping("/saveCustomer")
    public String saveCustomer(@ModelAttribute("customerDto") CustomerDto customerDto){
        customerService.saveCustomer(customerDto);
        return "redirect:/welcomeMsg";
    }

    @GetMapping("/welcomeMsg")
    public String welcomeMsg(){
        return "customer_home";
    }

    @GetMapping("/customerLoginForm")
    public String customerLoginForm(Model model){
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);
        return "customer_login_form";
    }

    @PostMapping("/customerLogin")
    public String customerLogin(@ModelAttribute("loginDto") LoginDto loginDto, HttpSession session){
        if (customerService.customerLogin(loginDto)){
            Customer customer = customerServiceImpl.findCustomerByEmail(loginDto.getEmail());
            session.setAttribute("customer_id", customer.getId());
            session.setAttribute("customer_name", customer.getName());
            return "redirect:/welcomeMsg";
        } else
            return "redirect:/customerLoginForm?error";
    }

    @PostMapping("/searchProductList")
    public String searchProductList(@Param(value = "keyword") String keyword, Model model){
        List<Product> listOfProducts = customerService.getProductByName(keyword);
        model.addAttribute("listOfProducts", listOfProducts);
        model.addAttribute("keyword", keyword);
        return "product_search";
    }

    @GetMapping("/addToCartForm/{id}")
    public String addToCartForm(@PathVariable(value = "id") long id, Model model){
        ProductDto productDto = customerService.getProductById(id);
        model.addAttribute("product", productDto);
        return "add_to_cart";
    }

    @PostMapping("/addToCart/{p_id}/{c_id}")
    public String addToCart(@PathVariable(value = "p_id") String product_id, @PathVariable(value = "c_id") String customer_id, @RequestParam String desiredQty){
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setProductId(Long.parseLong(product_id));
        addToCartDto.setCustomerId(Long.parseLong(customer_id));
        addToCartDto.setDesiredQty(Integer.parseInt(desiredQty));
        log.debug("Cart Dto ======================================>: {}",addToCartDto);
        customerService.addToCart(addToCartDto);
        return "redirect:/welcomeMsg";
    }

    @GetMapping("/customerCart/{c_id}")
    public String customerCart(@PathVariable(value = "c_id") String customer_id, Model model){
        model.addAttribute("cartList", customerService.findAllCartByCustomer(Long.parseLong(customer_id)));
        return "view_customer_cart";
    }
    @GetMapping("/sellCartProducts/{c_id}")
    public String sellCartProducts(@PathVariable(value = "c_id") String c_id){
        if (customerService.sellCartProducts(Long.parseLong(c_id))){
            return "redirect:/welcomeMsg?value=successful";
        }else
            return "redirect:/welcomeMsg?value=unsuccessful";
    }
    @GetMapping("/deleteCart/{cart_id}")
    public String deleteCart(@PathVariable(value = "cart_id") Long cart_id){
        customerService.deleteCart(cart_id);
        return "redirect:/customerCart?success";
    }
}
