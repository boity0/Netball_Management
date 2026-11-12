package com.netballmanager.netballmanager.dto;

import java.time.LocalDateTime;

public record TrainingResponse(Long id, Long teamId, String title, String venue, LocalDateTime startsAt) {
}
