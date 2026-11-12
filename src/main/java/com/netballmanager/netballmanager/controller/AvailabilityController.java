package com.netballmanager.netballmanager.controller;

import com.netballmanager.netballmanager.dto.AvailabilityRequest;
import com.netballmanager.netballmanager.dto.AvailabilitySummary;
import com.netballmanager.netballmanager.service.AvailabilityService;
import com.netballmanager.netballmanager.service.CurrentUserService;
import com.netballmanager.netballmanager.service.GameMatchService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches/{matchId}/availability")
public class AvailabilityController {
    private final AvailabilityService availability;
    private final CurrentUserService currentUser;
    private final GameMatchService matches;

    public AvailabilityController(AvailabilityService availability, CurrentUserService currentUser,
                                  GameMatchService matches) {
        this.availability = availability;
        this.currentUser = currentUser;
        this.matches = matches;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COACH', 'PLAYER')")
    public AvailabilitySummary respond(@PathVariable Long matchId,
                                       @Valid @RequestBody AvailabilityRequest request,
                                       Principal principal) {
        currentUser.requireOwnPlayerId(principal.getName(), request.playerId());
        currentUser.assertTeamAccess(principal.getName(), matches.get(matchId).teamId());
        return availability.respond(matchId, request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COACH', 'PLAYER')")
    public AvailabilitySummary summary(@PathVariable Long matchId, Principal principal) {
        currentUser.assertTeamAccess(principal.getName(), matches.get(matchId).teamId());
        return availability.summary(matchId);
    }
}
