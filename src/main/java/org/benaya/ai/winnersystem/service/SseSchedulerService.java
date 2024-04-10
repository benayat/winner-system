package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.events.SseEvent;

public interface SseSchedulerService {
    void queueSseMessage(SseEvent sseEvent);
}
