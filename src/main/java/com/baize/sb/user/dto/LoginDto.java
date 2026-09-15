package com.baize.sb.user.dto;

import lombok.Data;

@Data
public class LoginDto {
  private String name;
  private String password;

  private String uuid;
  private String code;
}
