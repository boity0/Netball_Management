package com.netballmanager.netballmanager.dto;

import com.netballmanager.netballmanager.entity.Position;

public record LineupPlayerResponse(Long playerId, String playerName, Position position, boolean starting) {
}
