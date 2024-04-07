package org.benaya.ai.winnersystem.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.events.PeriodBreakEvent;
import org.benaya.ai.winnersystem.service.BetsService;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class PeriodBreakListener {
    private final CacheManager cacheManager;
    @EventListener(value = PeriodBreakEvent.class)
    @Async
    public void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
        Objects.requireNonNull(cacheManager.getCache("betsControllerBlockCache")).put("blocked", periodBreakEvent.isBlockBets());
        log.info("Period Break Event");
    }
}
