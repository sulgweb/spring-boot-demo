package com.baize.sb.security;

import com.baize.sb.common.JwtUtil;
import com.baize.sb.user.User;
import com.baize.sb.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");
    if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
      String token = authorization.substring(BEARER_PREFIX.length()).trim();
      // Swagger's bearer scheme adds the prefix automatically, but tolerate users pasting it too.
      if (token.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
        token = token.substring(BEARER_PREFIX.length()).trim();
      }
      authenticate(token);
    }
    filterChain.doFilter(request, response);
  }

  private void authenticate(String token) {
    try {
      UUID userId = jwtUtil.parseUserId(token);
      User user = userRepository.findById(userId).orElse(null);
      if (user == null) {
        return;
      }

      AuthenticatedUser principal = new AuthenticatedUser(user.getId(), user.getName());
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(principal, null, List.of());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException | IllegalArgumentException ignored) {
      SecurityContextHolder.clearContext();
    }
  }
}
