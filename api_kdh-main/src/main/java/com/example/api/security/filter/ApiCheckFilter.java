package com.example.api.security.filter;

import com.example.api.security.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  private final String[] pattern;
  private final AntPathMatcher antPathMatcher;
  private final JWTUtil jwtUtil;

  public ApiCheckFilter(String[] pattern, JWTUtil jwtUtil) {
    this.antPathMatcher = new AntPathMatcher();
    this.pattern = pattern;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    log.info("요청 URI: {}", request.getRequestURI());
    boolean check = false;

    for (String p : pattern) {
      log.info("검사 중: 요청 URI = {}, 패턴 = {}", request.getRequestURI(), p);
      if (antPathMatcher.match(p, request.getRequestURI())) {
        check = true;
        break;
      }
    }

    if (check) {
      log.info("요청 URI가 패턴과 일치함: {}", request.getRequestURI());
      boolean checkTokenHeader = checkAuthHeader(request);

      if (checkTokenHeader) {
        filterChain.doFilter(request, response);
        return;
      } else {
        log.warn("API 토큰 검증 실패");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("{\"code\": \"403\", \"message\": \"API 토큰 검증 실패\"}");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean checkAuthHeader(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      log.info("Authorization 헤더: {}", authHeader);
      try {
        Claims claims = jwtUtil.validateAndExtract(authHeader.substring(7));
        String email = claims.get("sub", String.class);
        Long mid = claims.get("mid", Long.class); // mid 클레임 추출

        log.info("토큰 검증 결과: 이메일 = {}, mid = {}", email, mid);

        // 세션 스토리지에 mid 저장하기
        // 이 부분은 클라이언트 측에서 진행해야 합니다.

        return email != null && email.length() > 0;
      } catch (Exception e) {
        log.error("토큰 검증 중 오류 발생: {}", e.getMessage());
      }
    }
    return false;
  }


}