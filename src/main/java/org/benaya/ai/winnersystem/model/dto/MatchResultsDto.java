package org.benaya.ai.winnersystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;

@Getter
@Setter
@AllArgsConstructor
public class MatchResultsDto {
    private String team1Name;
    private String team2Name;
    private int team1Goals;
    private int team2Goals;
    private String winner;

    public MatchResultsDto(Match match, MatchResults matchResults) {
        this.team1Name = match.getTeam1();
        this.team2Name = match.getTeam2();
        this.team1Goals = matchResults.getTeam1Goals();
        this.team2Goals = matchResults.getTeam2Goals();
        this.winner = matchResults.getWinner().name();
    }
}