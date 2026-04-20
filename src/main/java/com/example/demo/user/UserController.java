package com.example.demo.user;

import com.example.demo.user.dto.CreateUserDto;
import com.example.demo.user.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  // 注册
  @PostMapping("/register")
  public User create(@RequestBody CreateUserDto dto) {
    return userService.create(dto);
  }

  // 登录
  @PostMapping("/login")
  public String login(@RequestBody LoginDto dto) {
    return userService.login(dto);
  }
}