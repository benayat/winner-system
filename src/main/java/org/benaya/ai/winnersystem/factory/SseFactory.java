package org.benaya.ai.winnersystem.factory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.events.EventType;
import org.benaya.ai.winnersystem.model.events.SseEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Slf4j
public class SseFactory {

    private final Map<String, SseEmitter> secureEmitters = new ConcurrentHashMap<>();

    private final Map<String, SseEmitter> simpleEmitters = new ConcurrentHashMap<>();

    public SseEmitter getSecureEmitter(String userName) {
        return secureEmitters.computeIfAbsent(userName, k -> createEmitter(k, secureEmitters));
    }

    public SseEmitter getSimpleEmitter(String ipAddress) {
        return simpleEmitters.computeIfAbsent(ipAddress, k -> createEmitter(k, simpleEmitters));
    }

    public Map<String, SseEmitter> getEmittersForEvent(SseEvent event) {
        if (event.getEventType().equals(EventType.PERIOD_BREAK_EVENT)) {
            return secureEmitters;
        } else {
            return simpleEmitters;
        }
    }

    public void removeSecureEmitter(String userName) {
        getSecureEmitter(userName).complete();
    }

    private SseEmitter createEmitter(String key, Map<String, SseEmitter> collectionToDeleteFrom) {
        SseEmitter emitter = new SseEmitter(600000L);
        emitter.onError((e) -> log.error("error on emitter: {}", e.getMessage()));
        emitter.onCompletion(() -> {
            log.info("emitter completed");
            collectionToDeleteFrom.remove(key);
        });
        return emitter;
    }
}
