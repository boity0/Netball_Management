package com.netballmanager.netballmanager.controller;

import com.netballmanager.netballmanager.dto.StatisticsRequest;
import com.netballmanager.netballmanager.dto.StatisticsResponse;
import com.netballmanager.netballmanager.service.CurrentUserService;
import com.netballmanager.netballmanager.service.StatisticsService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatisticsController {
    private final StatisticsService statistics;
    private final CurrentUserService currentUser;

    public StatisticsController(StatisticsService statistics, CurrentUserService currentUser) {
        this.statistics = statistics;
        this.currentUser = currentUser;
    }

    @PostMapping("/matches/{matchId}/statistics")
    @PreAuthorize("hasRole('COACH')")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public StatisticsResponse record(@PathVariable Long matchId,
                                     @Valid @RequestBody StatisticsRequest request) {
        return statistics.record(matchId, request);
    }

    @GetMapping("/players/{playerId}/statistics")
    @PreAuthorize("hasAnyRole('COACH', 'PLAYER')")
    public List<StatisticsResponse> history(@PathVariable Long playerId, Principal principal) {
        currentUser.requireOwnPlayerId(principal.getName(), playerId);
        return statistics.history(playerId);
    }
}
