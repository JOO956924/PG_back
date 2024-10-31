package com.example.api.security.filter;

import com.example.api.security.dto.ClubMemberAuthDTO;
import com.example.api.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
  private final JWTUtil jwtUtil;

  public ApiLoginFilter(String defaultFilterProcessUrl, JWTUtil jwtUtil) {
    super(defaultFilterProcessUrl);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    String email = request.getParameter("email");
    String pass = request.getParameter("pw");

    log.info("인증 시도: 이메일 = {}", email);

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pass);
    return getAuthenticationManager().authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    log.info("인증 성공: 사용자 = {}", authResult.getPrincipal());

    ClubMemberAuthDTO memberAuthDTO = (ClubMemberAuthDTO) authResult.getPrincipal();
    String email = memberAuthDTO.getEmail();
    Long mid = memberAuthDTO.getMid(); // mid 추출

    // 응답 JSON에 mid 포함
    String token;
    try {
      token = jwtUtil.generateToken(email, mid); // mid를 포함하여 토큰 생성
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"token\": \"" + token + "\", \"mid\": " + mid + "}");
      log.info("생성된 토큰: {}", token);
    } catch (Exception e) {
      log.error("토큰 생성 중 오류 발생: {}", e.getMessage());
      e.printStackTrace();
    }
  }




  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    log.warn("인증 실패: {}", failed.getMessage());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write("{\"error\": \"" + failed.getMessage() + "\"}");
  }
}