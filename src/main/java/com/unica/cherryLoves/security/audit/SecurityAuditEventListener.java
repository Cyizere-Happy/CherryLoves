package com.unica.cherryLoves.security.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityAuditEventListener {

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.info("SECURITY AUDIT: Authentication Success - User: {}", event.getAuthentication().getName());
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        log.warn("SECURITY AUDIT: Authentication Failure - User: {} - Reason: {}", 
                event.getAuthentication().getName(), event.getException().getMessage());
    }

    @EventListener
    public void onAuthorizationFailure(AuthorizationFailureEvent event) {
        log.error("SECURITY AUDIT: Authorization Failure - User: {} - Resource: {} - Reason: {}", 
                event.getAuthentication().getName(), event.getSource(), event.getAccessDeniedException().getMessage());
    }
}
