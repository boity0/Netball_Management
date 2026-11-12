package com.netballmanager.netballmanager.repository;

import com.netballmanager.netballmanager.entity.TrainingSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findAllByTeamIdOrderByStartsAt(Long teamId);
}
