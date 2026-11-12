package com.netballmanager.netballmanager.dto;

public record StatisticsResponse(Long matchId, Long playerId, String playerName, int goalsScored,
                                 int goalAttempts, int assists, int interceptions, int deflections,
                                 int rebounds, int turnovers, int penalties, int minutesPlayed) {
}
