package org.benaya.ai.winnersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Match implements Serializable {
    String team1;
    String team2;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Match match = (Match) obj;
        return (team1.equals(match.team1) && team2.equals(match.team2)) || (team1.equals(match.team2) && team2.equals(match.team1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(team1, team2) + Objects.hash(team2, team1);
    }
}