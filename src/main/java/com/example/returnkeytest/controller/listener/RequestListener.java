package com.example.returnkeytest.controller.listener;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.UUID;

@Component
public class RequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        String requestId = UUID.randomUUID().toString();
        sre.getServletRequest().setAttribute("X-Request-Id", requestId);
        MDC.put("requestId", requestId);
        ServletRequestListener.super.requestInitialized(sre);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ServletRequestListener.super.requestDestroyed(sre);
        MDC.clear();
    }
}
