package org.benaya.ai.winnersystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
public class Bet implements Serializable {
    @Id
    @EmbeddedId
    private BetId betId;
    @ManyToOne
    @MapsId("userName")
    private UserProfile userProfile;
    private String teamName;
    int betAmount;

    public Bet(UserProfile userProfile, String team1Name, String team2Name, String teamName, int betAmount) {
        this.betId = BetId.builder().userEmail(userProfile.getEmail()).team1Name(team1Name).team2Name(team2Name).build();
        this.userProfile = userProfile;
        this.teamName = teamName;
        this.betAmount = betAmount;
    }
}
