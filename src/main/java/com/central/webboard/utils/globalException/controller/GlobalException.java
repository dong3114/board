package com.central.webboard.utils.globalException.controller;

import com.central.webboard.utils.globalException.domain.ErrorLog;
import com.central.webboard.utils.globalException.service.ErrorLogService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 이 클래스는 RESTful API 통신 마다 에러 반환 시 DB 로그 추적 할 수 있게 설계된 클래스입니다.
 */
@RestControllerAdvice // @ControllerAdvice + @ResponseBody
public class GlobalException {

        private final ErrorLogService errorLogService;

        public GlobalException(ErrorLogService errorLogService) {
            this.errorLogService = errorLogService;
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
            // 에러 로그 저장
            ErrorLog log = ErrorLog.builder()
                    .uri(request.getRequestURI())
                    .method(request.getMethod())
                    .message(e.getMessage())
                    .stackTrace(Arrays.toString(e.getStackTrace()))
                    .createdAt(new Date())
                    .build();
            errorLogService.save(log);

            // 2. 예외 타입 분기 처리
            if (e instanceof ExpiredJwtException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "토큰이 만료되었습니다."));
            }

            if (e instanceof SignatureException || e instanceof MalformedJwtException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "유효하지 않은 JWT 토큰입니다."));
            }

            if (e instanceof AccessDeniedException) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "접근 권한이 없습니다."));
            }

            if (e instanceof DataIntegrityViolationException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "데이터베이스 제약 조건 위반입니다."));
            }

            // 3. 기본 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."));
        }
}
