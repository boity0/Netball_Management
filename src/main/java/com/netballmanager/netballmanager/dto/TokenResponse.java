package com.netballmanager.netballmanager.dto;

import java.time.Instant;

public record TokenResponse(String accessToken, String tokenType, Instant expiresAt) {
}
