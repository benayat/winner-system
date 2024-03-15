package org.benaya.ai.winnersystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bet{
    @Id
    @EmbeddedId
    private BetId betId;
    @ManyToOne
    @MapsId("userName")
    private UserProfile userProfile;
    private String teamName;
    int betAmount;
}
