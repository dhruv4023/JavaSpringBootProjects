
package com.authserver.authserver.user.app_initialization_service;

import com.authserver.authserver.base.app_initialization_service.AbstractStartupInitializer;
import com.authserver.authserver.base.enums.RoleEnum;
import com.authserver.authserver.user.models.AccessRights;
import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.repositories.AccessRightsRepository;
import com.authserver.authserver.user.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppInitializationService extends AbstractStartupInitializer {

    private final RoleRepository roleRepository;
    private final AccessRightsRepository accessRightsRepository;
    private final RequestMappingHandlerMapping handlerMapping;

    private final List<String> excludeRoutes = List.of(
            "/error", "/auth/signup", "/auth/login", "/auth/forgot-password", "/");

    private final List<String> adminExcludeRoutes = List.of("/role", "/user");

    @Override
    protected void initialize() {
        RoleModel adminRole = roleRepository.findByRoleName(RoleEnum.ADMIN.name());
        if (adminRole == null) {
            adminRole = roleRepository.save(
                    new RoleModel(RoleEnum.ADMIN.name(), "Admin role with admin access"));
        }

        if (roleRepository.findByRoleName(RoleEnum.SUPER_USER.name()) == null) {
            roleRepository.save(
                    new RoleModel(RoleEnum.SUPER_USER.name(), "Super User role with full access"));
            roleRepository.save(
                    new RoleModel(RoleEnum.USER.name(), "User role with limited access"));
        }

        RoleModel finalAdminRole = adminRole;

        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            var patternsCondition = requestMappingInfo.getPathPatternsCondition();
            if (patternsCondition != null) {
                patternsCondition.getPatternValues().forEach(url -> {

                    if (excludeRoutes.contains(url))
                        return;

                    String name = generateRightName(handlerMethod);
                    String description = handlerMethod.toString();

                    accessRightsRepository.findByName(name)
                            .orElseGet(() -> {
                                AccessRights r = new AccessRights();
                                r.setName(name);
                                r.setDescription(description);
                                r.setRoute(url);

                                if (!adminExcludeRoutes.stream().anyMatch(url::startsWith)) {
                                    finalAdminRole.getAccessRights().add(r);
                                }

                                return accessRightsRepository.save(r);
                            });
                });
            }
        });
    }

    private String generateRightName(HandlerMethod handlerMethod) {
        String controller = handlerMethod.getBeanType()
                .getSimpleName()
                .replace("Controller", "");
        String method = handlerMethod.getMethod().getName();
        return (controller + "_" + method).toUpperCase();
    }

}