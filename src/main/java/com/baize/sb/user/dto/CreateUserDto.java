package com.baize.sb.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {

  @NotBlank(message = "用户名不能为空")
  @Size(max = 100, message = "用户名不能超过 100 个字符")
  private String name;

  @NotNull(message = "年龄不能为空")
  @Min(value = 1, message = "年龄不能小于 1")
  @Max(value = 150, message = "年龄不能大于 150")
  private Integer age;

  @NotBlank(message = "密码不能为空")
  @Size(min = 8, max = 72, message = "密码长度必须在 8 到 72 个字符之间")
  private String password;

  @NotBlank(message = "验证码 ID 不能为空")
  private String uuid;

  @NotBlank(message = "验证码不能为空")
  private String code;
}
