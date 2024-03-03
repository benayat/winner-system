package org.benaya.ai.winnersystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Team {
    @Id
    private String name;
    private int points;
    private int goals;
    private float skillLevel;
    private int injuries;
}
