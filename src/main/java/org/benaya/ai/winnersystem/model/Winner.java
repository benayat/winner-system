package org.benaya.ai.winnersystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum Winner {
    TEAM1("TEAM1"),
    TEAM2("TEAM2"),
    TIE("TIE");
    private final String value;
    private static final Map<String, Winner> lookup = new HashMap<>();

    static {
        for (Winner status : Winner.values()) {
            lookup.put(status.getValue(), status);
        }
    }

    public static Winner get(String value) {
        return lookup.get(value.toUpperCase());
    }
}