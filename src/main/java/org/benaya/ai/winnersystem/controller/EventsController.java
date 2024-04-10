package org.benaya.ai.winnersystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.benaya.ai.winnersystem.model.events.TimerEvent;
import org.benaya.ai.winnersystem.model.events.Units;
import org.benaya.ai.winnersystem.service.SseSchedulerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse-events")
@Slf4j
public class EventsController {
    private final SseFactory sseFactory;
    private final SseSchedulerService sseSchedulerService;
    @GetMapping(value = "/bets", produces = "text/event-stream")
    @PreAuthorize("#user.username == authentication.principal.username")
    public SseEmitter getSecureEvents(@AuthenticationPrincipal(errorOnInvalidType = true)User user) {
        return sseFactory.getSecureEmitter(user.getUsername());
    }
    @GetMapping(value = "/round", produces = "text/event-stream")
    public SseEmitter getRoundEvents(HttpServletRequest request) {
        return sseFactory.getSimpleEmitter(request.getRemoteAddr());
    }
    @GetMapping(value = "/example", produces = "text/event-stream")
    public void sendExapmleEvent() {
        sseSchedulerService.queueSseMessage(new TimerEvent(1, Units.SECONDS));
    }
    @GetMapping("/num-emitters")
    public int getNumEmitters() {
        return sseFactory.getSimpleEmitters().size();
    }
}
