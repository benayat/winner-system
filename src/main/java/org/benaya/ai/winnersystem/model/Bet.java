package org.benaya.ai.winnersystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Bet implements Serializable {
    @Id
    @EmbeddedId
    private BetId betId;
    @ManyToOne
    @MapsId("userEmail")
    private UserProfile userProfile;
    private Winner winner;
    int betAmount;

    public Bet(String userEmail, String team1Name, String team2Name, UserProfile userProfile, Winner winner, int betAmount) {
        this.betId = BetId.builder().userEmail(userEmail).team1Name(team1Name).team2Name(team2Name).build();
        this.userProfile = userProfile;
        this.winner = winner;
        this.betAmount = betAmount;
    }
}