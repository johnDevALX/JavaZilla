package com.ekene.javazilla.controller;

import com.ekene.javazilla.model.Cashier;
import com.ekene.javazilla.model.Manager;
import com.ekene.javazilla.model.Product;
import com.ekene.javazilla.service.ManagerService;
import com.ekene.javazilla.service.ManagerServiceImpl;
import com.ekene.javazilla.utility.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private ManagerServiceImpl managerServiceImpl;

    @GetMapping("/managerSetUp")
    public String managerSetUp(Model model){
        Manager manager = new Manager();
        model.addAttribute("manager", manager);
        return "manager_setup";
    }

    @PostMapping("/saveManager")
    public String saveManager(@ModelAttribute("manager") Manager manager){
        managerService.saveManager(manager);
        return "manager_dashboard";
    }

    @GetMapping("/managerLoginForm")
    public String managerLoginForm(Model model){
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);
        return "manager_login";
    }

    @PostMapping("/getManagerByEmail")
    public String getManagerByEmail(@ModelAttribute("loginDto") LoginDto loginDto, HttpSession httpSession){
        if (managerService.loginManager(loginDto)){
            Manager manager = managerServiceImpl.findManagerByEmail(loginDto.getEmail());
            httpSession.setAttribute("id", manager.getId());
            return "manager_dashboard";
        } else {
            return "redirect:/managerLoginForm?error";
        }
    }

    @GetMapping("/applicantForm")
    public String applicantForm(Model model){
        ApplicantDto applicantDto = new ApplicantDto();
        model.addAttribute("applicantDto", applicantDto);
        return "applicant_form";
    }

    @PostMapping("/cashierHire")
    public String cashierHire(@ModelAttribute("applicantDto") ApplicantDto applicantDto){
        if (applicantDto.getAge() > 18 && applicantDto.getAge() < 30){
            if (applicantDto.getYearsOfExp() > 3){
                Cashier cashier = new Cashier();
                cashier.setFirstName(applicantDto.getFirstName());
                cashier.setLastName(applicantDto.getLastName());
                cashier.setEmail(applicantDto.getEmail());
                cashier.setSex(applicantDto.getSex());
                cashier.setRole(Role.CASHIER);
                managerService.hireCashier(cashier);
                return "redirect:/feedbackMsg?cashierHire";
            } else {
                return "redirect:/applicantForm?errorByExp";
            }
        } else {
            return "redirect:/applicantForm?errorByAge";
        }
    }

    @GetMapping("/fireForm")
    public String fireForm(Model model){
        FireDto fireDto = new FireDto();
        model.addAttribute("fireDto", fireDto);
        return "contract_termination";
    }

    @PostMapping("cashierFire")
    public String cashierFire(@ModelAttribute("fireDto") FireDto fireDto){
        if (managerService.fireCashier(fireDto)){
            return "redirect:/feedbackMsg?fireSuccessful";
        } else {
            return "redirect:/fireForm?fireUnsuccessful";
        }
    }

    @GetMapping("/feedbackMsg")
    public String feedbackMsg(){
        return "manager_dashboard";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "add_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("productDto") ProductDto productDto){
        managerService.addProduct(productDto);
        return "redirect:/feedbackMsg?productAddedSuccessfully";
    }

    @GetMapping("/getAllProducts")
    public String getAllProducts(Model model){
        model.addAttribute("listOfProducts", managerService.getAllProducts());
        return "view_all_products";
    }

    @GetMapping("/productUpdateForm/{id}")
    public String productUpdateForm(@PathVariable(value = "id") long id, Model model){
        Product product = managerService.getProductById(id);
        model.addAttribute("product", product);
        return "update_product";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id){
        managerService.deleteProduct(id);
        return "redirect:/getAllProducts?success";
    }

    @PostMapping("/updateProduct/{id}")
    public String updateProduct(@PathVariable(value = "id") Long id, @ModelAttribute ProductDto productDto){
        managerService.updateProduct(id, productDto);
        return "redirect:/feedbackMsg?productUpdatedSuccessfully";
    }
}
