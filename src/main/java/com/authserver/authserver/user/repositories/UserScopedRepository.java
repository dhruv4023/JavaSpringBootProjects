package com.authserver.authserver.user.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserScopedRepository<Entity, ID> extends JpaRepository<Entity, ID> {

    Page<Entity> findByUserId(Long userId, Pageable pageable);

}

