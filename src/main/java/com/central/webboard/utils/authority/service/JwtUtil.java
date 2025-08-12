package com.central.webboard.utils.authority.service;

import com.central.webboard.utils.authority.config.JwtToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Autowired
    private final JwtToken jwtToken;

    public String generateToken(String memberCode, int roleNumber) {
         return Jwts.builder()
                 .setSubject(memberCode)
                 .claim("roleNumber", roleNumber)
                 .setIssuer(jwtToken.getIssuer())
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(System.currentTimeMillis() + jwtToken.getExpiration()))
                 .signWith(SignatureAlgorithm.HS256, jwtToken.getSecret())
                 .compact();
    }
    // 토큰의 유효성 검사
    public boolean validateToekn (String token){
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 만료됨: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("JWT 서명 오류: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 잘못된 인자: " + e.getMessage());
        }
        return false;
    }
    // 회원 번호 추출
    public String extractMemberCode(String token) {return getClaims(token).getSubject();}
    // 회원 권한레벨 추출
    public int extractRoleNuber(String token) {return getClaims(token).get("roleNumber", Integer.class);}
    // 토큰 파싱
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtToken.getSecret())
                .parseClaimsJws(token.replace("Bearer", "").trim())
                .getBody();
    }
}
