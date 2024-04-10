package org.benaya.ai.winnersystem.model.events;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;

import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class GoalCycleEvent extends SseEvent{
    Map<Match, MatchResults> matchResults;

    public GoalCycleEvent(Map<Match, MatchResults> matchResults) {
        super(EventType.GOAL_CYCLE_EVENT);
        this.matchResults = matchResults;
    }
}
