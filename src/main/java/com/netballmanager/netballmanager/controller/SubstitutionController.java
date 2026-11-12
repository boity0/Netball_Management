package com.netballmanager.netballmanager.controller;

import com.netballmanager.netballmanager.dto.SubstitutionRequest;
import com.netballmanager.netballmanager.dto.SubstitutionResponse;
import com.netballmanager.netballmanager.service.SubstitutionService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/matches/{matchId}/substitutions")
@PreAuthorize("hasRole('COACH')")
public class SubstitutionController {
    private final SubstitutionService substitutions;

    public SubstitutionController(SubstitutionService substitutions) {
        this.substitutions = substitutions;
    }

    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public SubstitutionResponse record(@PathVariable Long matchId,
                                       @Valid @RequestBody SubstitutionRequest request) {
        return substitutions.record(matchId, request);
    }

    @GetMapping
    public List<SubstitutionResponse> list(@PathVariable Long matchId) {
        return substitutions.list(matchId);
    }
}
