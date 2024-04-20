package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.Team;
import org.benaya.ai.winnersystem.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/all")
    public List<Team> getAllTeams() {
        return teamService.findAllTeams();
    }

    @GetMapping("/sorted")
    public List<Team> getAllTeamsSortedByPointsDescGoalsDescName() {
        return teamService.getAllTeamsSortedByPointsDescGoalsDescName();
    }
}
