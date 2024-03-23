package org.benaya.ai.winnersystem.model;


import lombok.Data;

import java.util.ArrayList;

@Data
public class TeamMatchups {
    private final String teamName;
    private ArrayList<String> opponents;

    public TeamMatchups(String teamName) {
        this.teamName = teamName;
        this.opponents = new ArrayList<>();
    }
}
