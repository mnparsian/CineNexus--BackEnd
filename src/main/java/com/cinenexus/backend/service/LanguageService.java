package com.cinenexus.backend.service;

import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }
    public List<Language> getAllLanguage(){
        return languageRepository.findAll();
    }
}
