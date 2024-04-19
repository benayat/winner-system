package org.benaya.ai.winnersystem.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.events.*;
import org.benaya.ai.winnersystem.service.SseSchedulerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ListIterator;
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
    @Value("${game.number_of_goal_events_per_game}")
    private int numberOfGoalEventsPerGame;
    @EventListener(value = PeriodBreakEvent.class)
    @Async
    public void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
        Objects.requireNonNull(cacheManager.getCache("betsControllerBlockCache")).put("blocked", periodBreakEvent.isBlockBets());
        log.info("period break Event");
        sseSchedulerService.queueSseMessage(periodBreakEvent);
        if (!periodBreakEvent.isBlockBets()) {
            runTimerEvents(BREAK_TIME_IN_SECONDS, 1, Units.SECONDS, TimeUnit.SECONDS);
        }
    }
    @EventListener(value = GoalCyclesForPeriodEvent.class)
    @Async
    public void onGoalCyclesForPeriodEvent(GoalCyclesForPeriodEvent goalCyclesForPeriodEvent) {
        log.info("Goal Cycles For Period Event");
        List<GoalCycleEvent> allEvents = goalCyclesForPeriodEvent.getAllTempResultsForPeriod().stream().map(GoalCycleEvent::new).toList();
        ListIterator<GoalCycleEvent> iterator = allEvents.listIterator();
        for (int i = 0; i <= MATCH_TIME_IN_MINUTES; i++) {
            int finalI = i;
            Callable<Void> task = () -> {
                sseSchedulerService.queueSseMessage(new TimerEvent(finalI, Units.MINUTES));
                if(finalI % (MATCH_TIME_IN_MINUTES/numberOfGoalEventsPerGame) == 0 && iterator.hasNext()) {
                    sseSchedulerService.queueSseMessage(iterator.next());
                }
                return null;
            };
            try {
                scheduledExecutorService.schedule(task, 333, TimeUnit.MILLISECONDS).get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
    }
    @Async
    public void runTimerEvents(int numEvents, int delayTime, Units units, TimeUnit timeUnit) {
        for (int i = 0; i <= numEvents; i++) {
            int finalI = i;
            Callable<Void> task = () -> {
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
        sseSchedulerService.queueSseMessage(sseEvent);
    }
}