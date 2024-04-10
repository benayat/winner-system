package org.benaya.ai.winnersystem.listeners;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.events.*;
import org.benaya.ai.winnersystem.service.SseSchedulerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
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

    @EventListener(value = PeriodBreakEvent.class)
    @Async
    public void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
        Objects.requireNonNull(cacheManager.getCache("betsControllerBlockCache")).put("blocked", periodBreakEvent.isBlockBets());
        log.info("period break Event");
        if(!periodBreakEvent.isBlockBets()) {
            for (int i = 0; i < BREAK_TIME_IN_SECONDS; i++) {
                int finalI = i;
                scheduledExecutorService.schedule(() -> {
                    sseSchedulerService.queueSseMessage(new TimerEvent(finalI + 1, Units.SECONDS));
                }, 1, TimeUnit.SECONDS);
            }
        }
    }
    @EventListener(value = MatchStartedEvent.class)
    @Async
    public void onGameStartedEvent(MatchStartedEvent matchStartedEvent) {
        log.info("Game Started Event");
        for(int i=0; i<MATCH_TIME_IN_MINUTES; i++){
            int finalI = i;
            scheduledExecutorService.schedule(() -> {
                sseSchedulerService.queueSseMessage(new TimerEvent(finalI +1, Units.MINUTES));
            }, 330, TimeUnit.MILLISECONDS);
        }
    }
    @EventListener(SseEvent.class)
    @Async
    public void onSseEvent(SseEvent sseEvent){
        log.info("Received sse event: {}", sseEvent);
        sseSchedulerService.queueSseMessage(sseEvent);
    }

//    public void onGoalCycleEvent(GoalCycleEvent goalCycleEvent) {
//        log.info("Sent goal cycle event to all users");
//        sseFactory.getSimpleEmitters().forEach((ip,emitter) -> {
//            try {
//                emitter.send(goalCycleEvent);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//    public void onGameStartedEvent(GameStartedEvent gameStartedEvent) {
//        log.info("Sending gameStartedEvent to all users");
//        sseFactory.getSimpleEmitters().forEach((ip,emitter) -> {
//            try {
//                emitter.send(gameStartedEvent);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//    private void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
//        log.info("Sending periodBreakEvent to all users");
//        sseFactory.getSecureEmitters().forEach((userName, emitter)-> {
//            try {
//                emitter.send(periodBreakEvent);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

}
