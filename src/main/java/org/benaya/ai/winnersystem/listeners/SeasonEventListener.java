package org.benaya.ai.winnersystem.listeners;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.events.SeasonEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SeasonEventListener {

    private final CacheManager cacheManager;
    @EventListener(value = SeasonEvent.class)
    @Async
    public void onSeasonEvent(SeasonEvent seasonEvent) {
        Objects.requireNonNull(cacheManager.getCache("seasonCache")).put("active", seasonEvent.isSeasonActive());
    }

}
