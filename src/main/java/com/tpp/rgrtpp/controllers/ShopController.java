package com.tpp.rgrtpp.controllers;

import com.tpp.rgrtpp.models.Shop;
import com.tpp.rgrtpp.service.ShopService;
import com.tpp.rgrtpp.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CityService cityService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listShops(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("shops", shopService.getAllShops());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "shops";
    }

    @GetMapping("/add")
    public String addShopForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("shop", new Shop());
        model.addAttribute("cities", cityService.getAllCities());
        return "add-shop";
    }

    @PostMapping("/add")
    public String addShop(@AuthenticationPrincipal UserDetails userDetails,
                          @Valid @ModelAttribute("shop") Shop shop,
                          BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("cities", cityService.getAllCities());
            return "add-shop";
        }
        shopService.saveShop(shop);
        return "redirect:/shops";
    }

    @GetMapping("/edit/{id}")
    public String editShopForm(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id,
                               Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Shop shop = shopService.findShopById(id).orElse(null);    
				if (shop != null) {           
        model.addAttribute("shop", shop);
        model.addAttribute("cities", cityService.getAllCities());
        return "edit-shop";
    }		else {
			return "redirect:/shops";
	}
 }
    @PostMapping("/update/{id}")
    public String updateShop(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id,
                             @Valid @ModelAttribute("shop") Shop shop,
                             BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("cities", cityService.getAllCities());
            return "edit-shop";
        }
        shop.setId(id);
        shopService.updateShop(shop);
        return "redirect:/shops";
    }

    @GetMapping("/delete/{id}")
    public String deleteShop(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        shopService.deleteShop(id);
        return "redirect:/shops";
    }
    
}
