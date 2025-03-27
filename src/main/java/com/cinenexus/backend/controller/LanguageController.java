package com.cinenexus.backend.controller;

import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<Language>> getAllCountry() {
        return ResponseEntity.ok(languageService.getAllLanguage());
    }
}
