package com.example.returnkeytest.controller.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/swagger") || request.getRequestURI().startsWith("/v3/api-docs") || request.getRequestURI().startsWith("/h2")) {
            super.doFilter(request, response, filterChain);
            return;
        }

        String requestId = request.getHeader("X-Request-Id") == null ? UUID.randomUUID().toString() :
                request.getHeader("X-Request-Id");

        response.setHeader("X-Request-Id", requestId);

        MDC.put("requestId", requestId);

        ContentCachingRequestWrapper requestWrapper = wrapRequest(request);
        ContentCachingResponseWrapper responseWrapper = wrapResponse(response);

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(requestWrapper, responseWrapper, filterChain);
        }

        responseWrapper.copyBodyToResponse();

        MDC.clear();
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            sb.append("\nIncoming request from: ").append(requestWrapper.getRemoteAddr()).append("\n");
            logRequest(requestWrapper, sb);
        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;
            sb.append("\nResponse to: ").append(requestWrapper.getRemoteAddr()).append("\n");
            logResponse(responseWrapper, sb);
            sb.append("\n\nFinished in: ").append(timeTaken).append("ms");
            log.trace(sb.toString());
        }
    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper, StringBuilder sb) {
        String queryString = requestWrapper.getQueryString();
        if (queryString == null) {
            sb.append(String.format("%s %s", requestWrapper.getMethod(), requestWrapper.getRequestURI())).append("\n");
        } else {
            sb.append(String.format("%s %s?%s", requestWrapper.getMethod(), requestWrapper.getRequestURI(), queryString)).append("\n");
        }
        Collections.list(requestWrapper.getHeaderNames())
                .forEach(headerName ->
                        Collections.list(requestWrapper.getHeaders(headerName))
                                .forEach(headerValue -> sb.append(String.format("%s: %s", headerName, headerValue)).append("\n")));

        byte[] content = requestWrapper.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, sb);
        }
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper, StringBuilder sb) {
        int status = responseWrapper.getStatus();
        sb.append(String.format("Status: %s %s", status, HttpStatus.valueOf(status).getReasonPhrase())).append("\n");
        responseWrapper.getHeaderNames()
                .forEach(headerName ->
                        responseWrapper.getHeaders(headerName)
                                .forEach(headerValue -> sb.append(String.format("%s: %s", headerName, headerValue)).append("\n")));

        byte[] content = responseWrapper.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, sb);
        }
    }

    private void logContent(byte[] content, StringBuilder sb) {
        sb.append("Body: ").append("\n");
        try {
            Object ob = objectMapper.readValue(content, Object.class);

            objectMapper.registerModule(new JavaTimeModule())
                    .enable(SerializationFeature.INDENT_OUTPUT);

            sb.append(objectMapper.writeValueAsString(ob));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            sb.append(String.format("[%d bytes content]", content.length));
        } finally {
            sb.append("\n");
        }
    }

    private ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
