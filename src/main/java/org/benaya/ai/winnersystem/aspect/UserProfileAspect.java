package org.benaya.ai.winnersystem.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserProfileAspect {

    private final PasswordEncoder passwordEncoder;
    @Pointcut("execution(* org.benaya.ai.winnersystem.controller.UserController.createUser(..))")
    public void createUserPointcut() {
    }

    @Around("createUserPointcut()")
    public Object aroundCreateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        UserProfile userProfile = (UserProfile) joinPoint.getArgs()[0];
        userProfile.setPassword(passwordEncoder.encode(userProfile.getPassword()));
        return joinPoint.proceed();
    }
}