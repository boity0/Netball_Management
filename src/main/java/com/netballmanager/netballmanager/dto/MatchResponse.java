package com.netballmanager.netballmanager.dto;

import com.netballmanager.netballmanager.entity.MatchStatus;
import com.netballmanager.netballmanager.entity.VenueType;
import java.time.LocalDateTime;

public record MatchResponse(Long id, Long teamId, String opponentName, String venue,
                            LocalDateTime matchDateTime, VenueType venueType, MatchStatus status,
                            Integer teamScore, Integer opponentScore) {
}
