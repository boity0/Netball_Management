package com.netballmanager.netballmanager.dto;

import com.netballmanager.netballmanager.entity.AttendanceStatus;

public record AttendanceResponse(Long id, Long playerId, Long matchId, Long trainingId,
                                 AttendanceStatus status) {
}
