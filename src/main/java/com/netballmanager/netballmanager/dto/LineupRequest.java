package com.netballmanager.netballmanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LineupRequest(
        @NotEmpty List<@Valid LineupSelectionRequest> starting,
        List<@Valid LineupSelectionRequest> standby) {
}
