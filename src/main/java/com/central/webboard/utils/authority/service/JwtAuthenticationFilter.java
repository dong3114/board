package com.central.webboard.utils.authority.service;

import com.central.webboard.utils.authority.config.SpringSecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();    // 요청 URI
        String method = request.getMethod();            // 요청 HTTP 메서드
        List<String> authWhiteList = SpringSecurityConfig.getAuthWhiteList();           // 인증 예외 객체 생성

        System.out.println("dofilter 시작: request 메서드" + method + "request 요청 URI" + requestURI);

        // 1. OPTIONS 요청일때 필토 통과
        if(method.equalsIgnoreCase("OPTIONS")) {
            System.out.println("옵션요청통과");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        // 2. 인증 예외 요청일때 필터 통과
        if(authWhiteList.stream().anyMatch(r -> requestURI.matches(r.replace("**", ".*")))){
            System.out.println("[dofilter] 인증 예외 경로 접근: " + requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        // 3. 헤더 검사
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            // 인증정보가 없음
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 4. 토큰 유효성 검증
        String token = authorizationHeader.replace("Bearer", "");
        System.out.println("토큰 값 확인"+token);
        if(!jwtUtil.validateToekn(token)) {
            System.out.println("[dofilter] 토큰이 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String memberCode = jwtUtil.extractMemberCode(token);
        int roleNumber = jwtUtil.extractRoleNuber(token);
        String roleName = roleNumber == 1 ? "일반사용자" : "관리자";
        System.out.println("[dofilter] 인증성공 - 사용자: " + memberCode + " / 회원레벨: " + roleNumber);
        // 5. 인증 객체 설정 및 정보 추출
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        memberCode,
                        null,
                        List.of(new SimpleGrantedAuthority(roleName))
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
