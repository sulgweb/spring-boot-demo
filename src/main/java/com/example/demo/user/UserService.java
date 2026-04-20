package com.example.demo.user;

import com.example.demo.common.JwtUtil;
import com.example.demo.user.dto.CreateUserDto;
import com.example.demo.user.dto.LoginDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final StringRedisTemplate redisTemplate;

  private static final String CAPTCHA_PREFIX = "captcha:";

  public UserService(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      StringRedisTemplate redisTemplate) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.redisTemplate = redisTemplate;
  }

  // ========================
  // 注册（带验证码）
  // ========================
  public User create(CreateUserDto dto) {

    verifyCaptcha(dto.uuid, dto.code);

    User user = new User();
    user.setName(dto.name);
    user.setAge(dto.age);
    user.setPassword(passwordEncoder.encode(dto.password));

    return userRepository.save(user);
  }

  // ========================
  // 登录（带验证码）
  // ========================
  public String login(LoginDto dto) {

    verifyCaptcha(dto.uuid, dto.code);

    User user = userRepository.findByName(dto.name)
        .orElseThrow(() -> new RuntimeException("用户不存在"));

    if (!passwordEncoder.matches(dto.password, user.getPassword())) {
      throw new RuntimeException("密码错误");
    }

    return JwtUtil.generateToken(user.getName());
  }

  // ========================
  // 验证码校验（核心逻辑）
  // ========================
  private void verifyCaptcha(String uuid, String code) {

    String key = CAPTCHA_PREFIX + uuid;
    String redisCode = redisTemplate.opsForValue().get(key);
    if (code == null || code == "") {
      throw new RuntimeException("请输入验证码");
    }

    if (redisCode == null) {
      throw new RuntimeException("验证码已过期");
    }

    if (!redisCode.equalsIgnoreCase(code)) {
      throw new RuntimeException("验证码错误");
    }

    // 用完删除（防重复提交）
    redisTemplate.delete(key);
  }
}