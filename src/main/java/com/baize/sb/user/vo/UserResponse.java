package com.baize.sb.user.vo;

import com.baize.sb.user.User;
import java.util.UUID;

public record UserResponse(UUID id, String name, Integer age) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getAge());
  }
}
