package org.benaya.ai.winnersystem.listeners;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.benaya.ai.winnersystem.model.events.GameStartedEvent;
import org.benaya.ai.winnersystem.model.events.GoalCycleEvent;
import org.benaya.ai.winnersystem.model.events.PeriodBreakEvent;
import org.benaya.ai.winnersystem.model.events.SseEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseEventListener {

    private final SseFactory sseFactory;

    @EventListener(SseEvent.class)
    @Async
    public void onSseEvent(SseEvent sseEvent){
        switch(sseEvent.getEventType()){
            case GAME_STARTED_EVENT -> onGameStartedEvent((GameStartedEvent) sseEvent);
            case GOAL_CYCLE_EVENT -> onGoalCycleEvent((GoalCycleEvent) sseEvent);
            case PERIOD_BREAK_EVENT -> onPeriodBreakEvent((PeriodBreakEvent) sseEvent);
        }
    }
    public void onGoalCycleEvent(GoalCycleEvent goalCycleEvent) {
        log.info("Sent goal cycle event to all users");
        sseFactory.getSimpleEmitters().parallelStream().forEach(emitter -> {
            try {
                emitter.send(goalCycleEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void onGameStartedEvent(GameStartedEvent gameStartedEvent) {
        log.info("Sending gameStartedEvent to all users");
        sseFactory.getSimpleEmitters().parallelStream().forEach(emitter -> {
            try {
                emitter.send(gameStartedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
        log.info("Sending periodBreakEvent to all users");
        sseFactory.getSecureEmitters().forEach((userName, emitter)-> {
            try {
                emitter.send(periodBreakEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
