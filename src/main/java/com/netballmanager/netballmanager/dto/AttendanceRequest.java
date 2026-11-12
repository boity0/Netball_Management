package com.netballmanager.netballmanager.dto;

import com.netballmanager.netballmanager.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceRequest(@NotNull Long playerId, @NotNull AttendanceStatus status) {
}
