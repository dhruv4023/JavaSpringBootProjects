package com.authserver.authserver.code_note.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authserver.authserver.code_note.Models.TagModel;

public interface TagRepository extends JpaRepository<TagModel, Long> {
    
}
