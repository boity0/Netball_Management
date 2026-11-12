package com.netballmanager.netballmanager.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LineupResponse(Long matchId, List<LineupPlayerResponse> players,
                             LocalDateTime updatedAt, String message) {
}
