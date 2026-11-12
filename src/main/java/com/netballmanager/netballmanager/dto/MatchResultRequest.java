package com.netballmanager.netballmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MatchResultRequest(@NotNull @Min(0) Integer teamScore,
                                 @NotNull @Min(0) Integer opponentScore) {
}
