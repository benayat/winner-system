package org.benaya.ai.winnersystem.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {
    @Id
    private String name;
    private int points;
    private int goals;
    private float skillLevel;
    private int injuries;
}