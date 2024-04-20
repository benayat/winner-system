package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.Team;

import java.util.List;

public interface TeamService {
    void saveTeam(Team team);

    Team findTeamByName(String name);

    List<Team> findAllTeams();

    void saveAll(List<Team> teams);

    List<Team> getAllTeamsSortedByPointsDescGoalsDescName();
}