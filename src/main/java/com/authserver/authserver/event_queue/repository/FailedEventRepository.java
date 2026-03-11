package com.authserver.authserver.event_queue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.event_queue.models.FailedEvents;

@Repository
public interface FailedEventRepository extends JpaRepository<FailedEvents, Long>  {
   
}
