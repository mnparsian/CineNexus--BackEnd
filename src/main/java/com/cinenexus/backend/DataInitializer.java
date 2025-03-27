package com.cinenexus.backend;




import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.enumeration.UserStatusType;

import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.model.user.Role;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.model.user.UserStatus;
import com.cinenexus.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;

    public DataInitializer(RoleRepository roleRepository, UserStatusRepository userStatusRepository, UserRepository userRepository,PasswordEncoder passwordEncoder,CountryRepository countryRepository,LanguageRepository languageRepository) {
        this.roleRepository = roleRepository;
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.countryRepository = countryRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
//        initializeUserStatuses();  // مقداردهی اولیه UserStatus
//        initializeRoles();         // مقداردهی اولیه Roles
        initializeUsers();         // مقداردهی اولیه Users
    }

//    private void initializeUserStatuses() {
//        if (userStatusRepository.count() == 0) {
//            List<UserStatus> statuses = List.of(
//                    new UserStatus(null, UserStatusType.ACTIVE),
//                    new UserStatus(null, UserStatusType.BANNED),
//                    new UserStatus(null, UserStatusType.DEACTIVATED)
//            );
//            userStatusRepository.saveAll(statuses);
//            System.out.println("✅ User Statuses Initialized!");
//        }
//    }
//
//    private void initializeRoles() {
//        if (roleRepository.count() == 0) {
//            List<Role> roles = List.of(
//                    new Role(null, RoleType.ADMIN),
//                    new Role(null, RoleType.USER)
//            );
//            roleRepository.saveAll(roles);
//            System.out.println("✅ Roles Initialized!");
//        }
//    }

    private void initializeUsers() {
        Role userRole = roleRepository.findByName(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role USER not found!"));

        UserStatus activeStatus = userStatusRepository.findByName(UserStatusType.ACTIVE)
                .orElseThrow(() -> new RuntimeException("UserStatus ACTIVE not found!"));

        if (userRepository.count() == 0) {
            User user = new User();
            user.setName("Mahdi");
            user.setSurname("Nazari");
            user.setUsername("admin");
            user.setEmail("admin@example.com");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole(userRole);
            user.setStatus(activeStatus);
            Country country = countryRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("Country Not Found"));
            user.setCountry(country);
            Language language = languageRepository.findById(1L).orElseThrow(()->new EntityNotFoundException("Language Not Found"));
            user.setPreferredLanguage(language);

            userRepository.save(user);
            System.out.println("✅ User Initialized!");
        }
    }
}