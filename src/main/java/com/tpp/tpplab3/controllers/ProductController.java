package com.tpp.tpplab3.controllers;

import com.tpp.tpplab3.models.Product;
import com.tpp.tpplab3.service.ProductService;
import com.tpp.tpplab3.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("shops", shopService.getAllShops());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("shops", shopService.getAllShops());
            return "add-product";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.findProductById(id).orElse(null);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("shops", shopService.getAllShops());
            return "edit-product";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @Valid @ModelAttribute("product") Product product,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("shops", shopService.getAllShops());
            return "edit-product";
        }
        product.setId(id);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
