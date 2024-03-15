package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventsController {
    private final SseFactory sseFactory;

    @GetMapping("/sse-events")
    public SseEmitter getEvents(@AuthenticationPrincipal String email) {
        return sseFactory.getEmitter(email);
    }
}
