package com.cinenexus.backend.controller;

import com.cinenexus.backend.dto.chat.MessageResponseDTO;
import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryController {
    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountry() {
        return ResponseEntity.ok(countryService.getAllCountry());
    }
}
