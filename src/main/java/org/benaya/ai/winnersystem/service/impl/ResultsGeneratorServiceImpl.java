package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.*;
import org.benaya.ai.winnersystem.service.BetsService;
import org.benaya.ai.winnersystem.service.ResultsGeneratorService;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.random.RandomGenerator;

import static org.benaya.ai.winnersystem.constant.FormulaConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResultsGeneratorServiceImpl implements ResultsGeneratorService {

    private final TeamServiceImpl teamService;
    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();
    private final UserProfileService userProfileService;
    private final BetsService betsService;


    public Set<List<Match>> generateMatchUps() {
        List<Team> teams = new ArrayList<>(teamService.findAllTeams());
        int numberOfTeams = teams.size();
        Set<List<Match>> periods = new HashSet<>();

        for (int i = 0; i < numberOfTeams - 1; i++) {
            List<Match> period = new ArrayList<>();
            List<Team> teamsInThisPeriod = new ArrayList<>(teams);

            for (int j = 0; j < numberOfTeams / 2; j++) {
                Team team1 = teamsInThisPeriod.removeFirst();
                Team team2 = teamsInThisPeriod.removeFirst();
                Match match = new Match(team1.getName(), team2.getName());
                period.add(match);
            }

            periods.add(period);
            // Rotate the teams for the next period, keeping the first team fixed
            Team fixed = teams.removeFirst();
            teams.add(teams.removeFirst());
            teams.addFirst(fixed);
        }

        return periods;
    }

    public List<MatchChances> getMatchesChancesForPeriod(List<Match> period) {
        return period.stream().map(match -> getMatchChances(match.getTeam1(), match.getTeam2())).toList();
    }

    public Map<Match, Scorer> matchToScorerProbabilityMapForGoalEvent(List<Match> period) {
        Map<Match, Scorer> matchToScorers = new ConcurrentHashMap<>();
        for (Match match : period) {
            Scorer scorer = generateGoalByProbability(match.getTeam1(), match.getTeam2());
            matchToScorers.put(match, scorer);
        }
        return matchToScorers;
    }

    public ConcurrentHashMap<Match, List<MatchResults>> getResultsForAllGoalEventsInPeriod(List<Match> periodMatches, int numberOfEvents) {
        ConcurrentHashMap<Match, List<MatchResults>> matchToResults = new ConcurrentHashMap<>(periodMatches.size());
        for (int i = 0; i < numberOfEvents; i++) {
            Map<Match, Scorer> matchToScorerOneGoalTime = matchToScorerProbabilityMapForGoalEvent(periodMatches);
            matchToScorerOneGoalTime.forEach((match, scorer) -> {
                matchToResults.putIfAbsent(match, new ArrayList<>());
                MatchResults matchResults = matchToResults.get(match).isEmpty() ? new MatchResults(0, 0) : new MatchResults(matchToResults.get(match).getLast());
                if (scorer == Scorer.TEAM1) {
                    matchResults.setTeam1Goals(matchResults.getTeam1Goals() + 1);
                } else if (scorer == Scorer.TEAM2) {
                    matchResults.setTeam2Goals(matchResults.getTeam2Goals() + 1);
                }
                matchToResults.get(match).add(matchResults);
            });
        }
        return matchToResults;
    }

    public Map<Match, MatchResults> getFinalResultsFromAllGoalResults(Map<Match, List<MatchResults>> matchToListOfGoalResults) {
        Map<Match, MatchResults> matchToFinalResults = new HashMap<>();
        matchToListOfGoalResults.forEach((match, goalResults) -> {
            MatchResults finalResults = goalResults.getLast();
            matchToFinalResults.put(match, finalResults);
        });
        return matchToFinalResults;
    }

    private MatchChances getMatchChances(String team1Name, String team2Name) {
        log.debug("calculating match chances for " + team1Name + " vs " + team2Name);
        float team1GoalProbability = calculateGoalProbability(team1Name);
        float team2GoalProbability = calculateGoalProbability(team2Name);
        float totalProbability = team1GoalProbability + team2GoalProbability;
        team1GoalProbability = 0.9f * team1GoalProbability / totalProbability;
        team2GoalProbability = 0.9f * team2GoalProbability / totalProbability;
        return new MatchChances(team1Name, team2Name, (int) (team1GoalProbability * 100), (int) (team2GoalProbability * 100));
    }


    private float calculateGoalProbability(String teamName) {
        Team team = teamService.findTeamByName(teamName);
        float probability = BASE_PROBABILITY + team.getSkillLevel() * SKILL_PROBABILITY_MODIFIER_STEP;
        float injuryFactor = BASE_INJURY_FACTOR - team.getSkillLevel() * SKILL_INJURY_SCALE;
        float resultProbability = probability - injuryFactor * team.getInjuries();
        if (resultProbability > 0.1 && resultProbability < 0.9) {
            return resultProbability;
        } else if (resultProbability < 0.1) {
            return 0;
        } else {
            return 0.9f;
        }
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
    public void handlePeriodResults
    (Map<Match, MatchResults> matchToResults, List<MatchChances> matchesChances) {
        log.debug("handling period results");
        matchToResults.forEach((match, results) -> {
            Team team1 = teamService.findTeamByName(match.getTeam1());
            Team team2 = teamService.findTeamByName(match.getTeam2());
            int team1PeriodGoals = results.getTeam1Goals();
            int team2PeriodGoals = results.getTeam2Goals();
            team1.setGoals(team1.getGoals() + team1PeriodGoals);
            team2.setGoals(team2.getGoals() + team2PeriodGoals);
            int team1PeriodPoints = team1PeriodGoals > team2PeriodGoals ? 3 : team1PeriodGoals == team2PeriodGoals ? 1 : 0;
            int team2PeriodPoints = team2PeriodGoals > team1PeriodGoals ? 3 : team2PeriodGoals == team1PeriodGoals ? 1 : 0;
            team1.setPoints(team1.getPoints() + team1PeriodPoints);
            team2.setPoints(team2.getPoints() + team2PeriodPoints);
            team1.setSkillLevel(team1.getSkillLevel() - SKILL_MODIFIER_STEP + team1.getPoints() * SKILL_MODIFIER_STEP);
            team2.setSkillLevel(team2.getSkillLevel() - SKILL_MODIFIER_STEP + team2.getPoints() * SKILL_MODIFIER_STEP);
            team1.setInjuries(randomGenerator.nextInt(6));
            team2.setInjuries(randomGenerator.nextInt(6));
            teamService.saveTeam(team1);
            teamService.saveTeam(team2);
            MatchChances matchChances = matchesChances.stream().filter(matchChances1 -> matchChances1.getTeam1Name().equals(match.getTeam1()) && matchChances1.getTeam2Name().equals(match.getTeam2())).findFirst().orElseThrow();
            if (results.getWinner().equals(Winner.TEAM1)) {
                List<UserProfile> usersBetOnTeam1 = userProfileService.getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndWinnerName(match.getTeam1(), match.getTeam2(), Winner.TEAM1);
                usersBetOnTeam1.forEach(user -> {
                    int winAmount = user.getBets().stream().filter(bet -> bet.getBetId().getTeam1Name().equals(match.getTeam1()) && bet.getBetId().getTeam2Name().equals(match.getTeam2())).findFirst().map(Bet::getBetAmount).orElseThrow() * 100 / matchChances.getTeam1Chances();
                    user.setBalance(user.getBalance() + winAmount);
                });
                userProfileService.saveAll(usersBetOnTeam1);
            } else if (results.getWinner().equals(Winner.TEAM2)) {
                List<UserProfile> usersBetOnTeam2 = userProfileService.getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndWinnerName(match.getTeam1(), match.getTeam2(), Winner.TEAM2);
                usersBetOnTeam2.forEach(user -> {
                    int winAmount = user.getBets().stream().filter(bet -> bet.getBetId().getTeam1Name().equals(match.getTeam1()) && bet.getBetId().getTeam2Name().equals(match.getTeam2())).findFirst().map(Bet::getBetAmount).orElseThrow() * 100 / matchChances.getTeam2Chances();
                    user.setBalance(user.getBalance() + winAmount);
                });
                userProfileService.saveAll(usersBetOnTeam2);
            } else {
                List<UserProfile> usersBetOnTie = userProfileService.getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndWinnerName(match.getTeam1(), match.getTeam2(), Winner.TIE);
                usersBetOnTie.forEach(user -> {
                    int winAmount = user.getBets().stream().filter(bet -> bet.getBetId().getTeam1Name().equals(match.getTeam1()) && bet.getBetId().getTeam2Name().equals(match.getTeam2())).findFirst().map(Bet::getBetAmount).orElseThrow() * 100 / (100 - matchChances.getTeam1Chances() - matchChances.getTeam2Chances());
                    user.setBalance(user.getBalance() + winAmount);
                });
                userProfileService.saveAll(usersBetOnTie);
            }
        });
        betsService.deleteAllBets();
    }

    public List<MatchChances> getMatchChancesByUserBets(String userEmail) {
        List<Bet> userBets = betsService.getAllBetsByUserName(userEmail);
        log.info("bets for user: " + userEmail + " are: " + userBets);
        return userBets.stream().map(bet -> getMatchChances(bet.getBetId().getTeam1Name(), bet.getBetId().getTeam2Name())).toList();
    }

}

