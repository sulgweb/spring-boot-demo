package com.example.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(RuntimeException.class)
  public ApiResponse<?> handleRuntimeException(RuntimeException e) {
    // 记录日志
    logger.error("业务异常: {}", e.getMessage());
    // 返回错误信息给前端
    return ApiResponse.fail(e.getMessage());
  }
}