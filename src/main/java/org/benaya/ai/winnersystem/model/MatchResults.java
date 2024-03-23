package org.benaya.ai.winnersystem.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResults {
    int team1Goals;
    int team2Goals;

    public MatchResults(MatchResults matchResults) {
        this.team1Goals = matchResults.team1Goals;
        this.team2Goals = matchResults.team2Goals;
    }
}
