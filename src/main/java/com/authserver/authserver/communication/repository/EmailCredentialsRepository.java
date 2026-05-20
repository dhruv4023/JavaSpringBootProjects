package com.authserver.authserver.communication.repository;

import org.springframework.stereotype.Repository;

import com.authserver.authserver.base.BaseRepository;
import com.authserver.authserver.communication.models.EmailCredentials;

@Repository
public interface EmailCredentialsRepository extends BaseRepository<EmailCredentials, Long>  {

}
