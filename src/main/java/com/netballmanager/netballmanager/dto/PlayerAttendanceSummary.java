package com.netballmanager.netballmanager.dto;

public record PlayerAttendanceSummary(Long playerId, long attended, long recordedSessions,
                                      double attendancePercentage) {
}
