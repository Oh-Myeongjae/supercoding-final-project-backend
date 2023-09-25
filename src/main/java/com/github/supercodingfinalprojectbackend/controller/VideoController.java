package com.github.supercodingfinalprojectbackend.controller;

import com.github.supercodingfinalprojectbackend.service.VideoService;
import com.github.supercodingfinalprojectbackend.util.ResponseUtils;
import com.github.supercodingfinalprojectbackend.util.ResponseUtils.ApiResponse;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createSession(
        @Valid @RequestBody(required = false) Map<String, Object> params)
        throws OpenViduHttpException, OpenViduJavaClientException {
        return ResponseUtils.ok("세션 생성 성공",videoService.createSession(params));
    }
    @PostMapping("/enter/{sessionId}")
    public ResponseEntity<ApiResponse<String>> connection(
        @Valid @PathVariable("sessionId") String sessionId,
        @Valid @RequestBody(required = false) Map<String, Object> params)
        throws OpenViduJavaClientException, OpenViduHttpException {

        return ResponseUtils.ok("연결 성공",videoService.connection(sessionId,params));
    }
}