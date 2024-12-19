package com.tpp.tpplab3.controllers;

import com.tpp.tpplab3.models.City;
import com.tpp.tpplab3.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public String listCities(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "cities";
    }

    @GetMapping("/add")
    public String addCityForm(Model model) {
        model.addAttribute("city", new City());
        return "add-city";
    }

    @PostMapping("/add")
    public String addCity(@Valid @ModelAttribute("city") City city, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-city";
        }
        cityService.saveCity(city);
        return "redirect:/cities";
    }

    @GetMapping("/edit/{id}")
    public String editCityForm(@PathVariable("id") Integer id, Model model) {
        City city = cityService.findCityById(id).orElse(null);
        if (city != null) {
            model.addAttribute("city", city);
            return "edit-city";
        } else {
            return "redirect:/cities";
        }
    }

    @PostMapping("/update/{id}")
    public String updateCity(@PathVariable("id") Integer id, @Valid @ModelAttribute("city") City city,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit-city";
        }
        city.setCityId(id);
        cityService.updateCity(city);
        return "redirect:/cities";
    }

    @GetMapping("/delete/{id}")
    public String deleteCity(@PathVariable("id") Integer id) {
        cityService.deleteCity(id);
        return "redirect:/cities";
    }
}
