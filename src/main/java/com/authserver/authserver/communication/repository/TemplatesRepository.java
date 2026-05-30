package com.authserver.authserver.communication.repository;

import com.authserver.authserver.base.BaseRepository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.authserver.authserver.communication.models.TemplateModel;

@Repository
public interface TemplatesRepository extends BaseRepository<TemplateModel, UUID> {

}
