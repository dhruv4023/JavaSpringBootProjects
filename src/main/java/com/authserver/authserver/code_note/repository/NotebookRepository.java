package com.authserver.authserver.code_note.repository;

import com.authserver.authserver.code_note.Models.Notebook;
import com.authserver.authserver.user.repositories.UserScopedRepository;

public interface NotebookRepository extends UserScopedRepository<Notebook, Long> {
    
}
