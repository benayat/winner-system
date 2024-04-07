package org.benaya.ai.winnersystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MatchChances {
    @Id
    String team1Name;
    @Id
    String team2Name;
    int team1Chances;
    int team2Chances;
}
