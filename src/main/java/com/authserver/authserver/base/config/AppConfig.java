package com.authserver.authserver.base.config;

import com.authserver.authserver.base.enums.RoleEnum;
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
            if (roleRepository.findByRoleName(RoleEnum.SUPER_USER.name()) == null) {
                RoleModel adminRole = new RoleModel();
                adminRole.setRoleName(RoleEnum.SUPER_USER.name());
                adminRole.setDescription("Super User role with full access");
                roleRepository.save(adminRole);
            }
        };
    }
}
