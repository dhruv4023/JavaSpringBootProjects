package com.authserver.authserver.code_note.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.authserver.authserver.code_note.Models.CodeNote;

public interface CodeNoteRepository extends JpaRepository<CodeNote, Long> {
    List<CodeNote> findByIdInAndNotebookId(Set<Long> ids, Long notebookId);

    @Query("""
            SELECT MAX(n.position)
            FROM CodeNote n
            WHERE n.notebook.id = :notebookId
            """)
    Long findMaxPositionByNotebookId(@Param("notebookId") Long notebookId);

    @Modifying
    @Query(value = """
            UPDATE code_notes cn
            JOIN (
                SELECT id,
                       ROW_NUMBER() OVER (ORDER BY position ASC) * :gap AS new_position
                FROM code_notes
                WHERE user_id = :userId
            ) ranked ON cn.id = ranked.id
            SET cn.position = ranked.new_position
            """, nativeQuery = true)
    void rebalancePositions(@Param("userId") Long userId, @Param("gap") Long gap);

    Page<CodeNote> findByNotebookId(Long notebookId, Pageable pageable);
}
