package org.benaya.ai.winnersystem.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.benaya.ai.winnersystem.model.events.SseEvent;
import org.benaya.ai.winnersystem.service.SseSchedulerService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class SseSchedulerServiceImpl implements SseSchedulerService {

    private final SseFactory sseFactory;

    private final ConcurrentLinkedQueue<SseEvent> events = new ConcurrentLinkedQueue<>();

    @Override
    public void queueSseMessage(SseEvent sseEvent) {
        events.add(sseEvent);
    }

    @Scheduled(fixedDelay = 330)
    public void sendEvents() {
        SseEvent event;

        while ((event = events.poll()) != null) {
            log.debug("number of emitters: "+ sseFactory.getSimpleEmitters().size() + "and secure emitters: "+ sseFactory.getSecureEmitters().size());
            SseEvent finalEvent = event;
            log.debug("num events to send: "+ getEvents().size());
            log.info("sending event: "+ finalEvent);
            sseFactory.getEmittersForEvent(event).forEach((key, emitter) -> {
                try {
                    emitter.send(finalEvent);
                } catch (Exception e) {
                    emitter.complete();
                }
            });
        }
    }
}