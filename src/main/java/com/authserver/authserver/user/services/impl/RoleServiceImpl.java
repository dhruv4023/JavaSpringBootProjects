package com.authserver.authserver.user.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.repositories.RoleRepository;
import com.authserver.authserver.user.services.RoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleModel createRole(RoleModel roleModel) {
        return roleRepository.save(roleModel);
    }    
    public RoleModel getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<RoleModel> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public RoleModel updateRole(Long id, RoleModel updatedRole) {
        return roleRepository.findById(id).map(role -> {
            role.setRoleName(updatedRole.getRoleName());
            return roleRepository.save(role);
        }).orElse(null);  
    }

    public Boolean deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
