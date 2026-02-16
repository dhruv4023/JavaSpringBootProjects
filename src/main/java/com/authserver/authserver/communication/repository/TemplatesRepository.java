package com.authserver.authserver.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.communication.models.TemplateModel;

@Repository
public interface TemplatesRepository extends JpaRepository<TemplateModel, Long>  {
   
}
