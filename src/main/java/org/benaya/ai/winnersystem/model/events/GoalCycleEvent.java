package org.benaya.ai.winnersystem.model.events;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;
import org.benaya.ai.winnersystem.model.dto.MatchResultsDto;

import java.util.List;
import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class GoalCycleEvent extends SseEvent{
    List<MatchResultsDto> matchResults;

    public GoalCycleEvent(Map<Match, MatchResults> matchResults) {
        super(EventType.GOAL_CYCLE_EVENT);
        this.matchResults = matchResults.entrySet().stream()
                .map(entry -> new MatchResultsDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
