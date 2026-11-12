package com.netballmanager.netballmanager.dto;

import com.netballmanager.netballmanager.entity.Position;
import jakarta.validation.constraints.NotNull;

public record LineupSelectionRequest(@NotNull Long playerId, @NotNull Position position) {
}
