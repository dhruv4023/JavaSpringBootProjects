package com.authserver.authserver.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.communication.models.EmailCredentials;

@Repository
public interface EmailCredentialsRepository extends JpaRepository<EmailCredentials, Long>  {
    
}
