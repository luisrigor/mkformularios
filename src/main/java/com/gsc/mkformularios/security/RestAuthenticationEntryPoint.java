package com.gsc.mkformularios.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsc.mkformularios.constants.ApiErrorConstants;
import com.gsc.mkformularios.exceptions.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        logger.trace("Responding with unauthorized error to {}. Message - {}", request.getRequestURI(), ex.getMessage());
        ApiError apiError = new ApiError(ApiErrorConstants.NO_PERMISSIONS, HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(objectMapper.writeValueAsString(apiError));
        response.flushBuffer();
    }
}
