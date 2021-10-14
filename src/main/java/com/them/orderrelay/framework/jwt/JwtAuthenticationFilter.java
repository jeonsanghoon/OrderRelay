package com.them.orderrelay.framework.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.them.orderrelay.framework.base.dto.ResCode;
import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.util.Global;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


// import 생략

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (token != null && jwtTokenProvider.validateToken(token).getResCd().equals(ResCode.ok.toString())) {
            Authentication auth = this.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        else {
            String url = request.getRequestURI().toLowerCase(Locale.ROOT);
            authenticationReToken(response, url);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticationReToken(HttpServletResponse response, String url) throws IOException {
        if(url.contains("//api/v1/"))
        {

        }
        else if(url.contains("/vroong/api/v1/"))
        {

        }
        invalidAccess(response);
    }


    private void invalidAccess(ServletResponse response) throws IOException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        ResDto res = new ResDto("cm0003", Global.getMessageInfo().getMessage("cm0003") + "(Access Denied)");
        try (OutputStream os = httpServletResponse.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, res);
            os.flush();
        }
    }

    private Authentication getAuthentication(String token){
        token = token.indexOf("Bearer") >= 0 ? token.substring("Bearer ".length()) : token;
        Claims claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(token).getBody();
        return new UsernamePasswordAuthenticationToken(claims, "",makeAuthorities());
    }

    private List<GrantedAuthority> makeAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}

