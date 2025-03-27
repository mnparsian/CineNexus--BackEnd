package com.cinenexus.backend.service;

import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountry(){
        return countryRepository.findAll();
    }
}
