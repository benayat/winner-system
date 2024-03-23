package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse-events")
@Slf4j
public class EventsController {
    private final SseFactory sseFactory;

    @GetMapping(value = "/bets", produces = "text/event-stream")
    public SseEmitter getEvents(@AuthenticationPrincipal String email) {
        log.info("inside getEvents");
        return sseFactory.getSecureEmitter(email);
    }

    @GetMapping(value = "/round", produces = "text/event-stream")
    public SseEmitter getRoundEvents() {
        return sseFactory.getSimpleEmmiter();
    }


}
