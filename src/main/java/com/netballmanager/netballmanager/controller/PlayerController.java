package com.netballmanager.netballmanager.controller;

import com.netballmanager.netballmanager.dto.PlayerRequest;
import com.netballmanager.netballmanager.dto.PlayerResponse;
import com.netballmanager.netballmanager.service.CurrentUserService;
import com.netballmanager.netballmanager.service.PlayerService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    private final PlayerService players;
    private final CurrentUserService currentUser;

    public PlayerController(PlayerService players, CurrentUserService currentUser) {
        this.players = players;
        this.currentUser = currentUser;
    }

    @GetMapping("/api/teams/{teamId}/players")
    public List<PlayerResponse> list(@PathVariable Long teamId, Principal principal) {
        currentUser.assertTeamAccess(principal.getName(), teamId);
        return players.list(teamId);
    }

    @PostMapping("/api/teams/{teamId}/players")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('COACH')")
    public PlayerResponse create(@PathVariable Long teamId, @Valid @RequestBody PlayerRequest request) {
        return players.create(teamId, request);
    }

    @GetMapping("/api/players/{id}")
    @PreAuthorize("hasAnyRole('COACH', 'PLAYER')")
    public PlayerResponse get(@PathVariable Long id, Principal principal) {
        currentUser.requireOwnPlayerId(principal.getName(), id);
        return players.get(id);
    }

    @GetMapping("/api/players/me")
    @PreAuthorize("hasRole('PLAYER')")
    public PlayerResponse me(Principal principal) {
        return players.get(currentUser.requireLinkedPlayerId(principal.getName()));
    }

    @PutMapping("/api/players/{id}")
    @PreAuthorize("hasRole('COACH')")
    public PlayerResponse update(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        return players.update(id, request);
    }

    @DeleteMapping("/api/players/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('COACH')")
    public void deactivate(@PathVariable Long id) {
        players.deactivate(id);
    }
}
