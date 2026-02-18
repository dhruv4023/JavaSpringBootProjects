package com.authserver.authserver.code_note.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.authserver.authserver.code_note.Models.CodeNoteModel;

public interface CodeNoteRepository extends JpaRepository<CodeNoteModel, Long> {
    List<CodeNoteModel> findByIdInAndUser_Id(Set<Long> ids, Long userId);

    @Query("""
            SELECT MAX(n.position)
            FROM CodeNoteModel n
            WHERE n.user.id = :userId
            """)
    Long findMaxPositionByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = """
            UPDATE code_notes cn
            JOIN (
                SELECT id,
                       ROW_NUMBER() OVER (ORDER BY position ASC) * 1000 AS new_position
                FROM code_notes
                WHERE user_id = :userId
            ) ranked ON cn.id = ranked.id
            SET cn.position = ranked.new_position
            """, nativeQuery = true)
    void rebalancePositions(@Param("userId") Long userId);
}
