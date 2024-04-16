package org.benaya.ai.winnersystem.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SseLogoutHandler implements LogoutHandler {
    private final SseFactory sseFactory;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        authentication.setAuthenticated(false);
        sseFactory.removeSecureEmitter(authentication.getName());
        Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("remember-me")).findFirst().ifPresent(cookie -> {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
    }
}
