package org.benaya.ai.winnersystem.hander;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseLogoutHandler implements LogoutHandler {
    private final SseFactory sseFactory;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        sseFactory.removeEmitter(authentication.getName());
    }
}
