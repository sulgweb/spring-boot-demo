package com.baize.sb.auth;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baize.sb.captcha.CaptchaVO;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final StringRedisTemplate redisTemplate;

  private static final String CAPTCHA_KEY_PREFIX = "captcha:";

  AuthController(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @GetMapping("/captcha")
  public CaptchaVO captcha() {

    LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40);

    String code = captcha.getCode();
    String uuid = UUID.randomUUID().toString();

    // 存 redis（5分钟）
    redisTemplate.opsForValue()
        .set(CAPTCHA_KEY_PREFIX + uuid, code, 5, TimeUnit.MINUTES);

    CaptchaVO vo = new CaptchaVO();
    vo.setUuid(uuid);
    vo.setImg(captcha.getImageBase64());

    return vo;
  }
}
