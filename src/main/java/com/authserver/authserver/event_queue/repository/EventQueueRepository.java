package com.authserver.authserver.event_queue.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.authserver.authserver.event_queue.models.EventQueue;
import com.authserver.authserver.user.models.UserModel;

@Repository
public interface EventQueueRepository extends JpaRepository<EventQueue, Long> {

       @Query(value = """
                     SELECT *
                     FROM (
                         SELECT *,
                                ROW_NUMBER() OVER (PARTITION BY sender_id ORDER BY id) as rn
                         FROM event_queue
                         WHERE event_type = :eventType
                           AND next_retry_after < UTC_TIMESTAMP()
                     ) t
                     WHERE rn = 1
                     ORDER BY id
                     """, nativeQuery = true)
       Page<EventQueue> fetchBatchDistinctSender(
                     @Param("eventType") String eventType,
                     Pageable pageable);

       long countByEventType(String eventType);

       Page<EventQueue> findByEventTypeAndNextRetryAfterBefore(
                     String eventType,
                     Instant time,
                     Pageable pageable);

       @Query("""
                         SELECT e.eventType, COUNT(e)
                         FROM EventQueue e
                         WHERE e.status = 'PENDING'
                           AND e.nextRetryAfter <= CURRENT_TIMESTAMP
                         GROUP BY e.eventType
                     """)
       List<Object[]> countPendingGroupedByEventType();

       Page<EventQueue> findBySenderAndEventType(UserModel sender, String eventType, Pageable pageable);
}
