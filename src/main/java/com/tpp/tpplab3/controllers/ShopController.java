package com.tpp.tpplab3.controllers;
import com.tpp.tpplab3.models.Shop;
import com.tpp.tpplab3.service.ShopService;
import com.tpp.tpplab3.service.CityService;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
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

		@Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String listShops(Model model) {
        model.addAttribute("shops", shopService.getAllShops());
        return "shops";
    }

    @GetMapping("/add")
    public String addShopForm(Model model) {			// Отримання всіх міст
        model.addAttribute("shop", new Shop());
        model.addAttribute("cities", cityService.getAllCities());
        return "add-shop";
    }

    @PostMapping("/add")
public String addShop(@Valid @ModelAttribute("shop") Shop shop, BindingResult result, Model model) {
    if (result.hasErrors()) {
        model.addAttribute("cities", cityService.getAllCities());
        return "add-shop";
    }
    shopService.saveShop(shop);
    return "redirect:/shops";
}

    @GetMapping("/edit/{id}")
    public String editShopForm(@PathVariable("id") Integer id, Model model) {
        Shop shop = shopService.findShopById(id).orElse(null);
        if (shop != null) {
            model.addAttribute("shop", shop);
            model.addAttribute("cities", cityService.getAllCities());
            return "edit-shop";
        } else {
            return "redirect:/shops";
        }
    }

		@PostMapping("/update/{id}")
public String updateShop(@PathVariable("id") Integer id, @Valid @ModelAttribute("shop") Shop shop,
                         BindingResult result, Model model) {
    if (result.hasErrors()) {
        // Якщо є помилки валідації, передаємо список міст і повертаємось на форму редагування
        model.addAttribute("cities", cityService.getAllCities());
        return "edit-shop";
    }

    // Оновлюємо магазин
    shop.setId(id);
    shopService.updateShop(shop);
    return "redirect:/shops"; // Після оновлення перенаправляємо на список магазинів
}
@GetMapping("/delete/{id}")
    public String deleteShop(@PathVariable("id") Integer id) {
        shopService.deleteShop(id);
        return "redirect:/shops";
    }
		@PostMapping("/execute-query")
public String executeQuery(@RequestParam("sqlQuery") String sqlQuery, Model model) {
    try {
        // Виконання SQL-запиту через JdbcTemplate
        List<Map<String, Object>> result = jdbcTemplate.query(sqlQuery, new ColumnMapRowMapper());

        if (!result.isEmpty()) {
            // Додати результат запиту у модель
            model.addAttribute("queryResult", Map.of(
                "columns", result.get(0).keySet(), // Колонки результату
                "rows", result.stream().map(Map::values).toList() // Рядки результату
            ));
        } else {
            model.addAttribute("queryResult", null);
        }
    } catch (Exception e) {
        // Додати повідомлення про помилку у модель
        model.addAttribute("errorMessage", "Error executing query: " + e.getMessage());
    }

    // Отримати список магазинів і додати його у модель
    model.addAttribute("shops", shopService.getAllShops());
    return "shops"; // Повертаємо назву HTML-сторінки для відображення
}
}
