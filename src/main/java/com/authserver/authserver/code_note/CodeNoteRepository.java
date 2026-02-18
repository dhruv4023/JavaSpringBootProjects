package com.authserver.authserver.code_note;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeNoteRepository extends JpaRepository<CodeNoteModel, Long> {
    
}
