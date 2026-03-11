package com.authserver.authserver.event_queue.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.event_queue.models.EventQueue;

@Repository
public interface EventQueueRepository extends JpaRepository<EventQueue, Long> {

    @Query(value = """
            SELECT *
            FROM (
                SELECT *,
                       ROW_NUMBER() OVER (PARTITION BY sender_id ORDER BY id) as rn
                FROM event_queue
                WHERE event_type = :eventType
                  AND status = 'PENDING'
            ) t
            WHERE rn = 1
            ORDER BY id
            """, nativeQuery = true)
    Page<EventQueue> fetchBatchDistinctSender(
            @Param("eventType") String eventType,
            Pageable pageable);
}
