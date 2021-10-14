package com.them.orderrelay.framework.jwt;

import com.them.orderrelay.framework.base.dto.ResCode;
import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.util.Global;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Value("${spring.jwt.secret}")
    @Getter
    private String secretKey;

    @Value("${spring.jwt.tokenValid}")
    private long tokenValidMs; // 일주일만 토큰 유효

    @Value("${spring.jwt.refreshTokenValid}")
    private long refreshTokenValidMs; // 일주일만 토큰 유효

    public long getTokenValidMs() {
        return tokenValidMs;
    }

    public long getRefreshTokenValidMs() {
        return refreshTokenValidMs;
    }

    public LocalDateTime getTokenExpireDate(){
        return LocalDateTime.now().plusSeconds(this.tokenValidMs/1000);
    }
    public LocalDateTime getRefreshTokenExpireDate(){
        return LocalDateTime.now().plusSeconds(this.refreshTokenValidMs/1000);
    }

    public LocalDateTime getTokenExpireDate(long validExpireTimes){
        return LocalDateTime.now().plusSeconds(validExpireTimes/1000);
    }

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토 큰 생성
    public String createToken(String userPk)
    {
        return this.createToken(userPk, "ROLE_USER", true);
    }

    public String createRefreshToken(String userPk)
    {
        return this.createToken(userPk, "ROLE_USER", false);
    }
    public String createToken(String userPk, String role, Boolean isToken) {
        List<String> roles = new ArrayList<>();
        roles.add(role);
        long tokenValidTimes = isToken? tokenValidMs : refreshTokenValidMs;
        return createToken(userPk, roles, tokenValidTimes);
    }

    public String createToken(String userPk, List<String> roles, long tokenValidTimes) {

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();
        String jwtToken =  Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidTimes)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
        Global.getLogInfo().info(log, "토큰발행", jwtToken);
        return jwtToken;
    }

    public String createToken(String userPk, Long validExpireTimes)
    {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return this.createToken(userPk, roles, validExpireTimes);
    }



    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        String userPk = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return userPk;
    }

    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("authorization");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public ResDto validateToken(String jwtToken) {
        ResDto resDto = new ResDto();
        resDto.setResCd(ResCode.ok.toString());
        resDto.setResMsg("정상입니다.");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            if (claims.getBody().getExpiration().before(new Date())) {
                resDto.setResCd("-2");
                resDto.setResMsg("유효기간이 만료되었습니다.");
            }
        } catch (Exception e) {

            resDto.setResCd("-1");
            resDto.setResMsg("토큰정보가 잘못되었습니다.");
        }
        return resDto;
    }

    public boolean isValidateToken(String jwtToken){
        return this.validateToken(jwtToken).getResCd().equals(ResCode.ok.toString());
    }
}
