package org.benaya.ai.winnersystem.listeners;


import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.events.ChancesEvent;
import org.benaya.ai.winnersystem.model.events.GoalCycleEvent;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SseEventListener {

    private final SseFactory sseFactory;
    private final UserProfileService userProfileService;
    @EventListener(ChancesEvent.class)
    @Async
    public void onChancesEvent(ChancesEvent chancesEvent) {
        sseFactory.getEmitters().forEach((userName,emitter)-> {
            List<Match> matchesToSend = userProfileService.getAllBetsByUserName(userName).stream().map(bet -> bet.getBetId().getMatch()).toList();
            try {
                emitter.send(chancesEvent.getMatchChances().entrySet().stream()
                        .filter(entry -> matchesToSend.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @EventListener(GoalCycleEvent.class)
    @Async
    public void onGoalCycleEvent(GoalCycleEvent goalCycleEvent) {
        sseFactory.getEmitters().forEach((userName,emitter)-> {
            try {
                emitter.send(goalCycleEvent.getMatchResults());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
