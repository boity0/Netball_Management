package com.netballmanager.netballmanager.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TrainingRequest(@NotBlank String title, @NotBlank String venue,
                              @NotNull @Future LocalDateTime startsAt) {
}
