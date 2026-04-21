package com.baize.sb.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

  private static final Key key = Keys.hmacShaKeyFor(
      "baize-java-spring-boot-demo-longer-secret".getBytes());

  // 生成 token
  public static String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // 解析 token
  public static String parseToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
