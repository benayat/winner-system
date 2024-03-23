package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchChances;
import org.benaya.ai.winnersystem.model.MatchResults;
import org.benaya.ai.winnersystem.model.Scorer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface ResultsGeneratorService {
    Set<List<Match>> generateMatchUps();
    List<MatchChances> getMatchesChancesForPeriod(List<Match> period);
    Map<Match, Scorer> matchToScorerProbabilityMapForGoalEvent(List<Match> period);
    ConcurrentHashMap<Match, List<MatchResults>> getResultsForAllGoalEventsInPeriod(List<Match> periodMatches, int numberOfEvents);
    Map<Match, MatchResults> getFinalResultsFromAllGoalResults(Map<Match, List<MatchResults>> matchToListOfGoalResults);
    void handlePeriodResults(Map<Match, MatchResults> matchToResults);
}
