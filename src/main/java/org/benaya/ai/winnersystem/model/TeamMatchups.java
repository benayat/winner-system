package org.benaya.ai.winnersystem.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
@Data
public class TeamMatchups {
    private final String teamName;
    private ArrayList<String> opponents;
}
