package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchChances;
import org.benaya.ai.winnersystem.model.MatchResults;
import org.benaya.ai.winnersystem.model.events.*;
import org.benaya.ai.winnersystem.service.ResultsGeneratorService;
import org.benaya.ai.winnersystem.service.GameRunnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class GameRunnerServiceImpl implements GameRunnerService {
    private final ApplicationEventPublisher applicationEventPublisher;
    ResultsGeneratorService resultsGeneratorService;
    @Value("#{${game.num_teams}-1}")
    private int numberOfRoundsPerSeason;
    @Value("${game.number_of_goal_events_per_game}")
    private int numberOfGoalEventsPerGame;
    @Value("#{${game.length}/${game.number_of_goal_events_per_game}}")
    private long oneGoalEventTimeInSeconds;
    @Value("${game.break_between_periods_in_seconds}")
    private long breakBetweenPeriodsInSeconds;

    @Async
    public void startGame() {
        try{
            Set<List<Match>> allMatchups = resultsGeneratorService.generateMatchUps();
            for(List<Match> period : allMatchups){
                runOnePeriod(period);
            }
        } catch (InterruptedException e) {
            log.error("Error in timer stream", e);
        }
    }

    private void runOnePeriod(List<Match> matchesList) throws InterruptedException {
        applicationEventPublisher.publishEvent(new PeriodBreakEvent(false));
        Thread.sleep(Duration.ofSeconds(breakBetweenPeriodsInSeconds));
        applicationEventPublisher.publishEvent(new PeriodBreakEvent(true));
        applicationEventPublisher.publishEvent(new GameStartedEvent());
        List<MatchChances> chancesList = resultsGeneratorService.getMatchesChancesForPeriod(matchesList);
        applicationEventPublisher.publishEvent(new ChancesEvent(chancesList));
        ConcurrentHashMap<Match, List<MatchResults>> matchToListOfTempResults = resultsGeneratorService.getResultsForAllGoalEventsInPeriod(matchesList, numberOfGoalEventsPerGame);
        for(int i = 0; i < numberOfGoalEventsPerGame; i++){
            runOneGoalEvent(matchToListOfTempResults, i);
        }
        Map<Match, MatchResults> finalResults = resultsGeneratorService.getFinalResultsFromAllGoalResults(matchToListOfTempResults);
        resultsGeneratorService.handlePeriodResults(finalResults);
    }

    private void runOneGoalEvent(ConcurrentHashMap<Match, List<MatchResults>> matchToListOfTempResults, int index) throws InterruptedException {
        Map<Match, MatchResults> tempResults = new HashMap<>();
        matchToListOfTempResults.forEach((k, v) -> tempResults.put(k, v.get(index)));
        applicationEventPublisher.publishEvent(new GoalCycleEvent(tempResults));
        Thread.sleep(Duration.ofSeconds(oneGoalEventTimeInSeconds));
    }
}
