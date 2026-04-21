package com.baize.sb.user.dto;

public class CreateUserDto {
  public String name;
  public Integer age;
  public String password;

  public String uuid; // 验证码id
  public String code; // 验证码
}
