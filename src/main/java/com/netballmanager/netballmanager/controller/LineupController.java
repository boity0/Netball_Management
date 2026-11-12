package com.netballmanager.netballmanager.controller;

import com.netballmanager.netballmanager.dto.LineupRequest;
import com.netballmanager.netballmanager.dto.LineupResponse;
import com.netballmanager.netballmanager.service.LineupService;
import com.netballmanager.netballmanager.service.CurrentUserService;
import com.netballmanager.netballmanager.service.GameMatchService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches/{matchId}")
public class LineupController {
    private final LineupService lineups;
    private final CurrentUserService currentUser;
    private final GameMatchService matches;

    public LineupController(LineupService lineups, CurrentUserService currentUser, GameMatchService matches) {
        this.lineups = lineups;
        this.currentUser = currentUser;
        this.matches = matches;
    }

    @PostMapping("/lineup")
    @PreAuthorize("hasRole('COACH')")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public LineupResponse save(@PathVariable Long matchId, @Valid @RequestBody LineupRequest request) {
        return lineups.save(matchId, request);
    }

    @GetMapping("/lineup")
    public LineupResponse get(@PathVariable Long matchId, Principal principal) {
        currentUser.assertTeamAccess(principal.getName(), matches.get(matchId).teamId());
        return lineups.get(matchId);
    }

    @PostMapping("/generate-lineup")
    @PreAuthorize("hasRole('COACH')")
    public LineupResponse recommend(@PathVariable Long matchId) {
        return lineups.recommend(matchId);
    }
}
