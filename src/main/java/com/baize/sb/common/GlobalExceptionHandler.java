package com.baize.sb.common;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e) {
    return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
    FieldError fieldError = e.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
    String message = fieldError == null ? "请求参数错误" : fieldError.getDefaultMessage();
    return ResponseEntity.badRequest().body(ApiResponse.fail(message));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException e) {
    String message = e.getConstraintViolations().stream()
        .findFirst()
        .map(violation -> violation.getMessage())
        .orElse("请求参数错误");
    return ResponseEntity.badRequest().body(ApiResponse.fail(message));
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ApiResponse<?>> handleMalformedRequest(Exception e) {
    return ResponseEntity.badRequest().body(ApiResponse.fail("请求参数格式错误"));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {
    logger.error("未处理异常", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail("服务器内部错误"));
  }
}
