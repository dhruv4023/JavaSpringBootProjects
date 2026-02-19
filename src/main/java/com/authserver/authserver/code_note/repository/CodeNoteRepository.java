package com.authserver.authserver.code_note.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authserver.authserver.code_note.Models.CodeNoteModel;

public interface CodeNoteRepository extends JpaRepository<CodeNoteModel, Long> {
    
}
