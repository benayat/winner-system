package org.benaya.ai.winnersystem.factory;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
public class SseFactory {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter getEmitter(String userName) {
        return emitters.computeIfAbsent(userName, k->new SseEmitter());
    }


    public void removeEmitter(String userName) {
        emitters.remove(userName);
    }
}
