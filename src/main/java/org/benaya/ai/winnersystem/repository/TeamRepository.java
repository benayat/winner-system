package org.benaya.ai.winnersystem.repository;

import org.benaya.ai.winnersystem.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {
    @NonNull
    List<Team> findAll();

    @Query(value = "SELECT * FROM Team ORDER BY points DESC, goals DESC, name", nativeQuery = true)
    List<Team> getAllTeamsSortedByPointsDescGoalsDescName();

    Team findByName(String name);
}