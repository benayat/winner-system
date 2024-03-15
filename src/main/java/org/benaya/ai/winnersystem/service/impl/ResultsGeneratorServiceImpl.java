package org.benaya.ai.winnersystem.service.impl;

import org.benaya.ai.winnersystem.model.*;
import org.benaya.ai.winnersystem.model.events.ChancesEvent;
import org.benaya.ai.winnersystem.model.events.GoalCycleEvent;
import org.benaya.ai.winnersystem.service.ResultsGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

import static org.benaya.ai.winnersystem.constant.FormulaConstants.*;

@Service
public class ResultsGeneratorServiceImpl implements ResultsGeneratorService {

    private final TeamServiceImpl teamService;
    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();
    private final int numberOfTeams;
    private final Set<List<Match>> periods;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ResultsGeneratorServiceImpl(TeamServiceImpl teamService, ApplicationEventPublisher applicationEventPublisher) {
        this.teamService = teamService;
        List<Team> teams = teamService.findAllTeams();
        this.numberOfTeams = teams.size();
        this.periods = generateMatchUps(teams);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void startSeason() {
        SimpleAsyncTaskSchedulerBuilder schedulerBuilder = new SimpleAsyncTaskSchedulerBuilder()
                .taskTerminationTimeout(Duration.ofSeconds(30))
                .virtualThreads(true);
        try (SimpleAsyncTaskScheduler scheduler = schedulerBuilder.build()) {
            for (List<Match> period : periods) {
                Map<Match, MatchChances> matchToChances = period.stream().map(match -> Map.entry(match, getMatchChances(match.team1(), match.team2())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                this.applicationEventPublisher.publishEvent(new ChancesEvent(matchToChances));
                scheduler.wait(30000);
                scheduler.schedule(() -> runOnePeriod(period), TriggerContext::lastCompletion);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public MatchChances getMatchChances(String team1Name, String team2Name) {
        float team1GoalProbability = calculateGoalProbability(team1Name);
        float team2GoalProbability = calculateGoalProbability(team2Name);
        return new MatchChances(team1Name, team2Name, (int) (team1GoalProbability * 100), (int) (team2GoalProbability * 100));
    }

    private void runOnePeriod(List<Match> period) {
        Map<Match, List<Scorer>> matchToScorers = new HashMap<>();
        SimpleAsyncTaskSchedulerBuilder schedulerBuilder = new SimpleAsyncTaskSchedulerBuilder()
                .virtualThreads(true);
        try (SimpleAsyncTaskScheduler scheduler = schedulerBuilder.build()) {
            AtomicInteger counter = new AtomicInteger(0);
            scheduler.scheduleAtFixedRate(() -> {
                if (counter.getAndIncrement() < 8) {
                    Map<Match, Scorer> matchToScorerOneGoalTime = runOneGoalTime(period);
                    matchToScorerOneGoalTime.forEach((match, scorer) -> {
                        matchToScorers.putIfAbsent(match, new ArrayList<>());
                        matchToScorers.get(match).add(scorer);
                    });
                    this.applicationEventPublisher.publishEvent(new GoalCycleEvent(getTempResults(matchToScorers)));
                } else {
                    scheduler.close();

                }
            }, Duration.ofSeconds(3));
        }
        handlePeriodResults(matchToScorers);
    }

    private Map<Match, MatchResults> getTempResults(Map<Match, List<Scorer>> matchToScorers){
        Map<Match, MatchResults> matchToTempResults = new HashMap<>();
        matchToScorers.forEach((match, scorers) -> {
            int team1PeriodGoals = (int) scorers.stream().filter(scorer -> scorer == Scorer.TEAM1).count();
            int team2PeriodGoals = (int) scorers.stream().filter(scorer -> scorer == Scorer.TEAM2).count();
            matchToTempResults.put(match, new MatchResults(team1PeriodGoals, team2PeriodGoals));
        });
        return matchToTempResults;
    }
    private Map<Match, Scorer> runOneGoalTime(List<Match> period) {
        Map<Match, Scorer> matchToScorers = new HashMap<>();
        for (Match match : period) {
            Scorer scorer = generateGoalByProbability(match.team1(), match.team2());
            matchToScorers.put(match, scorer);
        }
        return matchToScorers;
    }
    private float calculateGoalProbability(String teamName) {
        Team team = teamService.findTeamByName(teamName);
        float probability = BASE_PROBABILITY + team.getSkillLevel() * SKILL_PROBABILITY_MODIFIER_STEP;
        float injuryFactor = BASE_INJURY_FACTOR - team.getSkillLevel() * SKILL_INJURY_SCALE;
        float resultProbability = probability - injuryFactor * team.getInjuries();
        return resultProbability > 0 ? resultProbability : 0;
    }
    private Scorer generateGoalByProbability(String team1Name, String team2Name) {
        float team1Probability = calculateGoalProbability(team1Name);
        float team2Probability = calculateGoalProbability(team2Name);
        float randomValue = randomGenerator.nextFloat();

        if (randomValue <= team1Probability) {
            return Scorer.TEAM1;
        } else if (randomValue <= team1Probability + team2Probability) {
            return Scorer.TEAM2;
        } else return Scorer.NONE;
    }

//    handling period results - setting parameters for each team at the end, and sending relevant results to UI if needed.
    private void handlePeriodResults(Map<Match, List<Scorer>> matchToScorers) {
        matchToScorers.forEach((match, scorers) -> {
            Team team1 = teamService.findTeamByName(match.team1());
            Team team2 = teamService.findTeamByName(match.team2());
            int team1PeriodGoals = (int) scorers.stream().filter(scorer -> scorer == Scorer.TEAM1).count();
            int team2PeriodGoals = (int) scorers.stream().filter(scorer -> scorer == Scorer.TEAM2).count();
            team1.setGoals(team1.getGoals() + team1PeriodGoals);
            team2.setGoals(team2.getGoals() + team2PeriodGoals);
            int team1PeriodPoints = team1PeriodGoals > team2PeriodGoals ? 3 : team1PeriodGoals == team2PeriodGoals ? 1 : 0;
            int team2PeriodPoints = team2PeriodGoals > team1PeriodGoals ? 3 : team2PeriodGoals == team1PeriodGoals ? 1 : 0;
            team1.setPoints(team1.getPoints() + team1PeriodPoints);
            team2.setPoints(team2.getPoints() + team2PeriodPoints);
            team1.setSkillLevel(team1.getSkillLevel() - SKILL_MODIFIER_STEP + team1.getPoints() * SKILL_MODIFIER_STEP);
            team2.setSkillLevel(team2.getSkillLevel() - SKILL_MODIFIER_STEP + team2.getPoints() * SKILL_MODIFIER_STEP);
            team1.setInjuries(randomGenerator.nextInt(6));
        });
    }
    private Set<List<Match>> generateMatchUps(List<Team> teams) {
        SortedSet<TeamMatchups> allTeamMatchups = new TreeSet<>(Comparator.comparing(teamMatchups1 -> teamMatchups1.getOpponents().size()));
        for (int i = 0; i < teams.size(); i++) {
            TeamMatchups teamMatchups = new TeamMatchups(teams.get(i).getName());
            for (int j = i + 1; j < teams.size(); j++) {
                teamMatchups.getOpponents().add(teams.get(j).getName());
            }
            allTeamMatchups.add(teamMatchups);
        }
        Set<List<Match>> periods = new HashSet<>();
        for (int i = 0; i < numberOfTeams - 1; i++) {
            List<Match> period = new ArrayList<>();
            for (int j = 0; j < numberOfTeams / 2; j++) {
                try {
                    String team1 = allTeamMatchups.getLast().getTeamName();
                    String team2 = allTeamMatchups.getLast().getOpponents().removeFirst();
                    Match match = new Match(team1, team2);
                    period.add(match);
                } catch (NoSuchElementException e) {
                    break;
                }
                periods.add(period);
            }
        }
        return periods;
    }
}

