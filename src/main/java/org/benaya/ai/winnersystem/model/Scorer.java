package org.benaya.ai.winnersystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum Scorer {
    TEAM1("TEAM1"),
    TEAM2("TEAM2"),
    NONE("NONE");


    private final String value;
    private static final Map<String, Scorer> lookup = new HashMap<>();

    static {
        for (Scorer status : Scorer.values()) {
            lookup.put(status.getValue(), status);
        }
    }

    public static Scorer get(String value) {
        return lookup.get(value.toUpperCase());
    }
}
