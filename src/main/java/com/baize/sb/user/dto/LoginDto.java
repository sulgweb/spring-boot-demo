package com.baize.sb.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

  @NotBlank(message = "用户名不能为空")
  private String name;

  @NotBlank(message = "密码不能为空")
  private String password;

  @NotBlank(message = "验证码 ID 不能为空")
  private String uuid;

  @NotBlank(message = "验证码不能为空")
  private String code;
}
