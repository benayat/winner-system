package org.benaya.ai.winnersystem.model.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GoalCyclesForPeriodEvent {
    private List<Map<Match, MatchResults>> allTempResultsForPeriod;
}
