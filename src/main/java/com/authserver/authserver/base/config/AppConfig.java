package com.authserver.authserver.base.config;

import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initializeRoles() {
        return args -> {
            if (roleRepository.findByRoleName("super_user") == null) {
                RoleModel adminRole = new RoleModel();
                adminRole.setRoleName("super_user");
                adminRole.setDescription("Super User role with full access");
                roleRepository.save(adminRole);
            }
        };
    }
}
