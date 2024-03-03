package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchChances;
import org.benaya.ai.winnersystem.model.MatchResults;
import org.benaya.ai.winnersystem.service.WebsocketPublishService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebsocketPublishServiceImpl implements WebsocketPublishService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    public void publishGoalCycleResultsToUI(Map<Match, MatchResults> tempResultsMap) {
        simpMessagingTemplate.convertAndSend("/topic/goal-event", tempResultsMap);
    }
    public void publishChancesForPeriodGamesToUI(Map<Match, MatchChances> chancesMap) {
        simpMessagingTemplate.convertAndSend("/topic/chances-for-period", chancesMap);
    }
}
