package org.benaya.ai.winnersystem.model.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;

import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class GoalCycleEvent extends SseEvent{
    Map<Match, MatchResults> matchResults;
}
