package org.benaya.ai.winnersystem.factory;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
@Getter
public class SseFactory {

    private final Map<String, SseEmitter> secureEmitters = new ConcurrentHashMap<>();

    private final ConcurrentLinkedDeque<SseEmitter> simpleEmitters = new ConcurrentLinkedDeque<>();
    public SseEmitter getSecureEmitter(String userName) {
        return secureEmitters.computeIfAbsent(userName, k->new SseEmitter());
    }
    public SseEmitter getSimpleEmmiter() {
        SseEmitter emitter = new SseEmitter();
        simpleEmitters.add(emitter);
        return emitter;
    }


    public void removeSecureEmitter(String userName) {
        secureEmitters.remove(userName);
    }
}
