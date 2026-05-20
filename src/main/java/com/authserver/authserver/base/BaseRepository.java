package com.authserver.authserver.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<Entity, ID> extends JpaRepository<Entity, ID>, JpaSpecificationExecutor<Entity> {

}

