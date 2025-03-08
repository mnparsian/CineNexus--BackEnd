package com.cinenexus.backend.service;


import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.enumeration.UserStatusType;
import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.model.user.Role;
import com.cinenexus.backend.model.user.UserStatus;
import com.cinenexus.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserStatusRepository userStatusRepository;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;

    public DataSeeder(RoleRepository roleRepository, UserStatusRepository userStatusRepository,
                      CountryRepository countryRepository, LanguageRepository languageRepository) {
        this.roleRepository = roleRepository;
        this.userStatusRepository = userStatusRepository;
        this.countryRepository = countryRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initializeUserStatuses();
        initializeRoles();
        initializeCountries();
        initializeLanguages();
    }

    private void initializeUserStatuses() {
        if (userStatusRepository.count() == 0) {
            List<UserStatus> statuses = List.of(
                    new UserStatus(null, UserStatusType.ACTIVE),
                    new UserStatus(null, UserStatusType.BANNED),
                    new UserStatus(null, UserStatusType.DEACTIVATED)
            );
            userStatusRepository.saveAll(statuses);
            System.out.println("✅ User Statuses Initialized!");
        }
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = List.of(
                    new Role(null, RoleType.ADMIN),
                    new Role(null, RoleType.MODERATOR),
                    new Role(null, RoleType.USER)
            );
            roleRepository.saveAll(roles);
            System.out.println("✅ Roles Initialized!");
        }
    }

    private void initializeCountries() {
        if (countryRepository.count() == 0) {
            List<Country> countries = List.of(
                    new Country("United States"),
                    new Country("United Kingdom"),
                    new Country("Germany"),
                    new Country("France"),
                    new Country("Spain"),
                    new Country("Italy"),
                    new Country("Iran")
            );
            countryRepository.saveAll(countries);
            System.out.println("✅ Countries Initialized!");
        }
    }

    private void initializeLanguages() {
        if (languageRepository.count() == 0) {
            List<Language> languages = List.of(
                    new Language("en","English"),
                    new Language("sp","Spanish"),
                    new Language("fr","French"),
                    new Language("gr","German"),
                    new Language("it","Italian"),
                    new Language("per","Persian"),
                    new Language("ch","Chinese")
            );
            languageRepository.saveAll(languages);
            System.out.println("✅ Languages Initialized!");
        }
    }
}

