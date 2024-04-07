package org.benaya.ai.winnersystem.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.events.ChancesEvent;
import org.benaya.ai.winnersystem.service.BetsService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class ChancesEventListener {
    private final BetsService betsService;

    @EventListener(value = ChancesEvent.class)
    @Async
    public void onChancesEvent() {
        betsService.deleteAllBets();
        log.info("done with internal chances event");
    }
}
