package com.netballmanager.netballmanager.repository;

import com.netballmanager.netballmanager.entity.MatchLineup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchLineupRepository extends JpaRepository<MatchLineup, Long> {
    Optional<MatchLineup> findByMatchId(Long matchId);
}
