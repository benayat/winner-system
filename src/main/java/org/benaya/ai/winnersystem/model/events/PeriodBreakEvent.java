package org.benaya.ai.winnersystem.model.events;
import lombok.*;
import org.benaya.ai.winnersystem.model.MatchChances;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class PeriodBreakEvent extends SseEvent{
    boolean blockBets;
    List<MatchChances> matchChancesForUpcomingPeriod;
    public PeriodBreakEvent(boolean blockBets, List<MatchChances> matchChancesForUpcomingPeriod) {
        super(EventType.PERIOD_BREAK_EVENT);
        this.blockBets = blockBets;
        this.matchChancesForUpcomingPeriod = matchChancesForUpcomingPeriod;
    }
    public PeriodBreakEvent(boolean blockBets) {
        super(EventType.PERIOD_BREAK_EVENT);
        this.blockBets = blockBets;
        this.matchChancesForUpcomingPeriod = null;
    }
}