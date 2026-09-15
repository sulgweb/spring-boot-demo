package com.baize.sb.user.dto;

import lombok.Data;

@Data
public class CreateUserDto {
  private String name;
  private Integer age;
  private String password;

  private String uuid; // 验证码id
  private String code; // 验证码
}
