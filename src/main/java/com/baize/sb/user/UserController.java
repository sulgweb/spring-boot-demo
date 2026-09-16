package com.baize.sb.user;

import com.baize.sb.user.dto.CreateUserDto;
import com.baize.sb.user.dto.LoginDto;
import com.baize.sb.user.vo.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // 注册
  @PostMapping("/register")
  public UserResponse create(@Valid @RequestBody CreateUserDto dto) {
    return UserResponse.from(userService.create(dto));
  }

  // 登录
  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginDto dto) {
    return userService.login(dto);
  }
}
