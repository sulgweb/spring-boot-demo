package com.baize.sb.user;

import com.baize.sb.common.JwtUtil;
import com.baize.sb.common.BusinessException;
import com.baize.sb.user.dto.CreateUserDto;
import com.baize.sb.user.dto.LoginDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final StringRedisTemplate redisTemplate;
  private final JwtUtil jwtUtil;

  private static final String CAPTCHA_PREFIX = "captcha:";

  public UserService(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      StringRedisTemplate redisTemplate,
      JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.redisTemplate = redisTemplate;
    this.jwtUtil = jwtUtil;
  }

  // ========================
  // 注册（带验证码）
  // ========================
  public User create(CreateUserDto dto) {

    verifyCaptcha(dto.getUuid(), dto.getCode());
    if (userRepository.existsByName(dto.getName().trim())) {
      throw new BusinessException("用户名已存在");
    }

    User user = new User();
    user.setName(dto.getName().trim());
    user.setAge(dto.getAge());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));

    return userRepository.save(user);
  }

  // ========================
  // 登录（带验证码）
  // ========================
  public String login(LoginDto dto) {

    verifyCaptcha(dto.getUuid(), dto.getCode());

    User user = userRepository.findByName(dto.getName())
        .orElseThrow(() -> new BusinessException("用户不存在"));

    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new BusinessException("密码错误");
    }

    return jwtUtil.generateToken(user.getId());
  }

  // ========================
  // 验证码校验（核心逻辑）
  // ========================
  private void verifyCaptcha(String uuid, String code) {

    String key = CAPTCHA_PREFIX + uuid;
    String redisCode = redisTemplate.opsForValue().get(key);
    if (code == null || code.isBlank()) {
      throw new BusinessException("请输入验证码");
    }

    if (redisCode == null) {
      throw new BusinessException("验证码已过期");
    }

    if (!redisCode.equalsIgnoreCase(code)) {
      throw new BusinessException("验证码错误");
    }

    // 用完删除（防重复提交）
    redisTemplate.delete(key);
  }
}
