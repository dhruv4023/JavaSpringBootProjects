package com.authserver.authserver.event_queue.repository;

import com.authserver.authserver.base.BaseRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.event_queue.models.FinalStageEvents;

@Repository
public interface FailedEventRepository extends BaseRepository<FinalStageEvents, Long> {

}
