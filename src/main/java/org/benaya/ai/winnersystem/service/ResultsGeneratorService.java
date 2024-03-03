package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.MatchChances;

public interface ResultsGeneratorService {
    void startSeason();
    MatchChances getMatchChances(String team1Name, String team2Name);
}
