package org.benaya.ai.winnersystem.service;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchTempResults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebsocketPublishService {
    private final SimpMessagingTemplate simpMessagingTemplate;
//todo: check correctness in UI.
    public void publishGoalCycleResultsToUI(Map<Match, MatchTempResults> tempResultsMap) {
        simpMessagingTemplate.convertAndSend("/topic/goal-event", tempResultsMap);
    }
}
