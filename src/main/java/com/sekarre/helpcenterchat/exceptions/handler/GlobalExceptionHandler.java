package com.sekarre.helpcenterchat.exceptions.handler;

import com.sekarre.helpcenterchat.exceptions.AppRuntimeException;
import com.sekarre.helpcenterchat.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @MessageExceptionHandler(Throwable.class)
    @SendToUser(value = "/topic/private.errors")
    public ErrorMessage handleWebSocketException(Throwable e) {
        log.error(e.getMessage());
        return getCustomErrorMessage(e.getMessage());
    }

    @ExceptionHandler(value = AppRuntimeException.class)
    public ResponseEntity<ErrorMessage> handleAppRuntimeException(AppRuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(getCustomErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(UsernameNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(getCustomErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(getCustomErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(getCustomErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ErrorMessage getCustomErrorMessage(String e) {
        return ErrorMessage.builder()
                .cause(e)
                .timestamp(DateUtil.getCurrentDateTime())
                .build();
    }
}
