package com.github.supercodingfinalprojectbackend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.supercodingfinalprojectbackend.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class FilterExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        log.info("Api 요청! {} {}", request.getMethod(), request.getRequestURI());
        try {
            doFilter(request, response, filterChain);
            log.info("Api 응답! {}", HttpStatus.valueOf(response.getStatus()));
        }
        catch (ApiException e) {
            int status = e.getStatus();
            ResponseUtils.ApiResponse<?> data = new ResponseUtils.ApiResponse<>(status, e.getMessage(), null);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(status);
            response.getOutputStream().write(new ObjectMapper()
                    .writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
            log.info("Api 응답! {}", HttpStatus.valueOf(response.getStatus()));
            if (HttpStatus.valueOf(status).is5xxServerError()) e.printStackTrace();
        }
        catch (Exception e) {
            ResponseUtils.ApiResponse<?> data = new ResponseUtils.ApiResponse<>(500, e.getMessage(), null);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(500);
            response.getOutputStream().write(new ObjectMapper()
                    .writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
            log.info("Api 응답! {}", HttpStatus.valueOf(response.getStatus()));
            e.printStackTrace();
        }
    }
}
