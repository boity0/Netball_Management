package com.netballmanager.netballmanager.controller;

import com.netballmanager.netballmanager.dto.AttendanceRequest;
import com.netballmanager.netballmanager.dto.AttendanceResponse;
import com.netballmanager.netballmanager.dto.PlayerAttendanceSummary;
import com.netballmanager.netballmanager.dto.TrainingRequest;
import com.netballmanager.netballmanager.dto.TrainingResponse;
import com.netballmanager.netballmanager.service.AttendanceService;
import com.netballmanager.netballmanager.service.CurrentUserService;
import com.netballmanager.netballmanager.service.TrainingService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AttendanceController {
    private final AttendanceService attendance;
    private final TrainingService training;
    private final CurrentUserService currentUser;

    public AttendanceController(AttendanceService attendance, TrainingService training,
                                CurrentUserService currentUser) {
        this.attendance = attendance;
        this.training = training;
        this.currentUser = currentUser;
    }

    @PostMapping("/teams/{teamId}/training-sessions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('COACH')")
    public TrainingResponse createTraining(@PathVariable Long teamId,
                                           @Valid @RequestBody TrainingRequest request) {
        return training.create(teamId, request);
    }

    @GetMapping("/teams/{teamId}/training-sessions")
    public List<TrainingResponse> training(@PathVariable Long teamId, Principal principal) {
        currentUser.assertTeamAccess(principal.getName(), teamId);
        return training.list(teamId);
    }

    @PostMapping("/matches/{matchId}/attendance")
    @PreAuthorize("hasRole('COACH')")
    public AttendanceResponse recordMatch(@PathVariable Long matchId,
                                          @Valid @RequestBody AttendanceRequest request) {
        return attendance.recordMatch(matchId, request);
    }

    @PostMapping("/training-sessions/{sessionId}/attendance")
    @PreAuthorize("hasRole('COACH')")
    public AttendanceResponse recordTraining(@PathVariable Long sessionId,
                                             @Valid @RequestBody AttendanceRequest request) {
        return attendance.recordTraining(sessionId, request);
    }

    @GetMapping("/players/{playerId}/attendance")
    @PreAuthorize("hasAnyRole('COACH', 'PLAYER')")
    public List<AttendanceResponse> history(@PathVariable Long playerId, Principal principal) {
        currentUser.requireOwnPlayerId(principal.getName(), playerId);
        return attendance.history(playerId);
    }

    @GetMapping("/players/{playerId}/attendance/summary")
    @PreAuthorize("hasAnyRole('COACH', 'PLAYER')")
    public PlayerAttendanceSummary summary(@PathVariable Long playerId, Principal principal) {
        currentUser.requireOwnPlayerId(principal.getName(), playerId);
        return attendance.summary(playerId);
    }
}
