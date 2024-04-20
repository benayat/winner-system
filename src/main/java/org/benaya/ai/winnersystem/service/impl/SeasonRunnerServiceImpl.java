package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchChances;
import org.benaya.ai.winnersystem.model.MatchResults;
import org.benaya.ai.winnersystem.model.events.*;
import org.benaya.ai.winnersystem.service.ResultsGeneratorService;
import org.benaya.ai.winnersystem.service.SeasonRunnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.benaya.ai.winnersystem.constant.MatchConstants.BREAK_TIME_IN_SECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class SeasonRunnerServiceImpl implements SeasonRunnerService {
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ResultsGeneratorService resultsGeneratorService;
    @Value("${game.number_of_goal_events_per_game}")
    private int numberOfGoalEventsPerGame;
    @Value("${game.length}")
    private int matchLengthInSeconds;

    @Override
    public boolean isSeasonActive() {
        Cache.ValueWrapper activeWrapper = Objects.requireNonNull(cacheManager.getCache("seasonCache")).get("active");
        return Objects.equals(activeWrapper != null ? activeWrapper.get() : null, Boolean.TRUE);
    }

    @Async
    public void startSeason() {
        try {
            applicationEventPublisher.publishEvent(new SeasonEvent(true));
            Set<List<Match>> allMatchups = resultsGeneratorService.generateMatchUps();
            for (List<Match> period : allMatchups) {
                runOnePeriod(period);
                applicationEventPublisher.publishEvent(new MatchEndedEvent());
            }
        } catch (InterruptedException e) {
            log.error("Error in timer stream", e);
        } finally {
            applicationEventPublisher.publishEvent(new SeasonEvent(false));
        }
    }

    private void runOnePeriod(List<Match> matchesList) throws InterruptedException {
        List<MatchChances> chancesList = resultsGeneratorService.getMatchesChancesForPeriod(matchesList);
        log.info("in runOnePeriod method, Starting period");
        applicationEventPublisher.publishEvent(new PeriodBreakEvent(false, chancesList));
        Thread.sleep(Duration.ofSeconds(BREAK_TIME_IN_SECONDS));
        applicationEventPublisher.publishEvent(new PeriodBreakEvent(true));
        applicationEventPublisher.publishEvent(new MatchStartedEvent(matchesList));
        ConcurrentHashMap<Match, List<MatchResults>> matchToListOfTempResults = resultsGeneratorService.getResultsForAllGoalEventsInPeriod(matchesList, numberOfGoalEventsPerGame);
        List<Map<Match, MatchResults>> allTempResults = new ArrayList<>();
        for (int i = 0; i < numberOfGoalEventsPerGame; i++) {
            allTempResults.add(i, getTempResultsForOneGoalEvent(matchToListOfTempResults, i));
        }
        applicationEventPublisher.publishEvent(new GoalCyclesForPeriodEvent(allTempResults));
        Thread.sleep(Duration.ofSeconds(matchLengthInSeconds));
        Map<Match, MatchResults> finalResults = resultsGeneratorService.getFinalResultsFromAllGoalResults(matchToListOfTempResults);
        resultsGeneratorService.handlePeriodResults(finalResults, chancesList);
    }

    private Map<Match, MatchResults> getTempResultsForOneGoalEvent(ConcurrentHashMap<Match, List<MatchResults>> matchToListOfTempResults, int index) {
        Map<Match, MatchResults> tempResults = new HashMap<>();
        matchToListOfTempResults.forEach((k, v) -> tempResults.put(k, v.get(index)));
        return tempResults;
    }
}