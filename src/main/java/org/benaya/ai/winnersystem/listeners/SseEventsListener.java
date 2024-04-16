package org.benaya.ai.winnersystem.listeners;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.events.*;
import org.benaya.ai.winnersystem.service.SseSchedulerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.benaya.ai.winnersystem.constant.MatchConstants.BREAK_TIME_IN_SECONDS;
import static org.benaya.ai.winnersystem.constant.MatchConstants.MATCH_TIME_IN_MINUTES;


@Component
@RequiredArgsConstructor
@Slf4j
public class SseEventsListener {

    private final SseSchedulerService sseSchedulerService;
    private final CacheManager cacheManager;
    @Qualifier("blockingExecutor")
    private final ScheduledExecutorService scheduledExecutorService;
//    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener(value = PeriodBreakEvent.class)
    @Async
    public void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
        Objects.requireNonNull(cacheManager.getCache("betsControllerBlockCache")).put("blocked", periodBreakEvent.isBlockBets());
        log.info("period break Event");
        if (!periodBreakEvent.isBlockBets()) {
            runTimerEvents(BREAK_TIME_IN_SECONDS, 1, Units.SECONDS, TimeUnit.SECONDS);
        }
    }


    @EventListener(value = MatchStartedEvent.class)
    @Async
    public void onGameStartedEvent() {
        log.debug("Game Started Event");
        runTimerEvents(MATCH_TIME_IN_MINUTES, 330, Units.MINUTES, TimeUnit.MILLISECONDS);
    }

    private void runTimerEvents(int numEvents, int delayTime, Units units, TimeUnit timeUnit) {
        for (int i = 0; i <= numEvents; i++) {
            int finalI = i;
            Callable<Void> task = () -> {
//                applicationEventPublisher.publishEvent(new TimerEvent(finalI, units));
                sseSchedulerService.queueSseMessage(new TimerEvent(finalI, units));
                return null;
            };
            try {
                scheduledExecutorService.schedule(task, delayTime, timeUnit).get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
    }

    @EventListener(classes = {SseEvent.class, PeriodBreakEvent.class})
    @Async
    public void onSseEvent(SseEvent sseEvent) {
//        if(sseEvent.getEventType().equals(EventType.GOAL_CYCLE_EVENT)) log.info("sending goal cycle event");
        sseSchedulerService.queueSseMessage(sseEvent);
    }
}
