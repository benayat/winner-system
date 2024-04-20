package org.benaya.ai.winnersystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class MatchChances {
    @Id
    String team1Name;
    @Id
    String team2Name;
    int team1Chances;
    int team2Chances;
    int tieChances;

    public MatchChances(String team1Name, String team2Name, int team1Chances, int team2Chances) {
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.team1Chances = team1Chances;
        this.team2Chances = team2Chances;
        this.tieChances = (team1Chances + team2Chances) <= 100 ? 100 - (team1Chances + team2Chances) : 0;
    }
}