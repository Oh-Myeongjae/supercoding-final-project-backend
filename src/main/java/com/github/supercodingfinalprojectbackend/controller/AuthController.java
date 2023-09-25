package com.github.supercodingfinalprojectbackend.controller;

import com.github.supercodingfinalprojectbackend.dto.Login;
import com.github.supercodingfinalprojectbackend.dto.TokenDto;
import com.github.supercodingfinalprojectbackend.entity.type.UserRole;
import com.github.supercodingfinalprojectbackend.exception.ApiException;
import com.github.supercodingfinalprojectbackend.exception.errorcode.ApiErrorCode;
import com.github.supercodingfinalprojectbackend.service.Oauth2Service;
import com.github.supercodingfinalprojectbackend.util.ResponseUtils;
import com.github.supercodingfinalprojectbackend.util.ValidateUtils;
import com.github.supercodingfinalprojectbackend.util.auth.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "인증 API")
public class AuthController {

    private final Oauth2Service oauth2Service;

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 로그인")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(name = "code", required = false) @Parameter(name = "카카오 인가 코드") String code,
            @RequestParam(name = "error", required = false) @Parameter(name = "인증 실패 시 에러 코드") String error,
            @RequestParam(name = "error_description", required = false) @Parameter(name = "인증 실패 시 에러 메세지") String errorDescription,
            @RequestParam(name = "state", required = false) @Parameter(name = "요청시 전달한 state값과 동일한 값") String state
    ){
        if (code != null) {
            Login.Response response = oauth2Service.kakaoLogin(code);
            return ResponseUtils.ok("로그인에 성공했습니다.", response);
        } else {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("error", error);
            body.add("errorDescription", errorDescription);
            return ResponseUtils.badRequest("카카오 인가 코드를 받는 데 실패했습니다.", body);
        }
    }

    @PostMapping("/login/google")
    @Operation(summary = "구글 로그인")
    public ResponseEntity<ResponseUtils.ApiResponse<Login.Response>> googleLogin(@RequestParam @Parameter(name = "구글 액세스 토큰", required = true) String token) {
//        Login.Response response = oauth2Service.googleLogin(token);
//        return ResponseUtils.ok("로그인에 성공했습니다.", response);
        return null;
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<ResponseUtils.ApiResponse<Void>> logout() {
        Long userId = AuthUtils.getUserId();
        if (userId == 1004 || userId == 5252) throw new ApiException(400, "슈퍼토큰은 로그아웃이 불가능합니다!");
        oauth2Service.logout(userId);
        return ResponseUtils.ok("로그아웃에 성공했습니다.", null);
    }

    @GetMapping("/switch/{roleName}")
    @Operation(summary = "역할 전환")
    public ResponseEntity<ResponseUtils.ApiResponse<Login.Response>> switchRole(
            @PathVariable(name = "roleName") @Parameter(name = "역할 이름", required = true) String roleName
    ) {
        Long userId = AuthUtils.getUserId();
        if (userId == 1004 || userId == 5252) throw new ApiException(400, "슈퍼토큰은 역할 전환이 불가능합니다!");
        UserRole userRole = ValidateUtils.requireNotNull(UserRole.parseType(roleName), 400, "userRoleName은 다음 중 하나여야 합니다. " + UserRole.getScopeAsString());
        if (!oauth2Service.hasRole(userId, userRole)) throw ApiErrorCode.DOES_NOT_HAVE_ROLL.exception();
        Login.Response response = oauth2Service.switchRole(userId, userRole);
        return ResponseUtils.ok("역할을 성공적으로 전환했습니다.", response);
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "액세스 토큰 갱신")
    public ResponseEntity<ResponseUtils.ApiResponse<TokenDto.Response>> renewTokens(
            @RequestBody @Parameter(name = "토큰 갱신 요청 객체", required = true) TokenDto.RefreshTokenRequest request
    ) {
        ValidateUtils.requireTrue(request.validate(), ApiErrorCode.INVALID_REQUEST_BODY);
        String refreshToken = request.getRefreshToken();

        TokenDto.Response response =  oauth2Service.renewTokens(refreshToken);
        return ResponseUtils.ok("토큰이 성공적으로 갱신되었습니다.", response);
    }
}
