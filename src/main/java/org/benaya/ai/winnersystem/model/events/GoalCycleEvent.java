package org.benaya.ai.winnersystem.model.events;

import lombok.*;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCycleEvent{
    Map<Match, MatchResults> matchResults;
}
