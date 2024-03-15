package org.benaya.ai.winnersystem.model.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchChances;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChancesEvent {
    private Map<Match, MatchChances> matchChances;
}
