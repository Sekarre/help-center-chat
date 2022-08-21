package com.sekarre.helpcenterchat.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ChatListenerExceptionHandler {

    @Around(value = "@annotation(com.sekarre.helpcenterchat.exceptions.handler.EventListenerErrorHandling)")
    public Object handleException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
