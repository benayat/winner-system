package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.Team;
import org.benaya.ai.winnersystem.repository.TeamRepository;
import org.benaya.ai.winnersystem.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Override
    public Team findTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    @Override
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public void saveAll(List<Team> teams) {
        teamRepository.saveAll(teams);
    }
}
