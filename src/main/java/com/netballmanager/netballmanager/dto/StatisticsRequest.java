package com.netballmanager.netballmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StatisticsRequest(
        @NotNull Long playerId, @Min(0) int goalsScored, @Min(0) int goalAttempts,
        @Min(0) int assists, @Min(0) int interceptions, @Min(0) int deflections,
        @Min(0) int rebounds, @Min(0) int turnovers, @Min(0) int penalties,
        @Min(0) int minutesPlayed) {
}
