package com.github.supercodingfinalprojectbackend.controller;

import com.github.supercodingfinalprojectbackend.service.Oauth2Service;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final Oauth2Service oauth2Service;

    @GetMapping("/oauth2/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestParam(name = "code") String code){
        System.out.println(code);
        return oauth2Service.kakaoLogin(code);
    }

    @GetMapping("/test")
    public String test(){
        return "성공";
    }
}
