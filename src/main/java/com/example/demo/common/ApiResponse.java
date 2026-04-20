package com.example.demo.common;

public class ApiResponse<T> {
  public int code;
  public String message;
  public T data;

  public static <T> ApiResponse<T> ok(T data) {
    ApiResponse<T> r = new ApiResponse<>();
    r.code = 0;
    r.message = "success";
    r.data = data;
    return r;
  }

  public static <T> ApiResponse<T> fail(String msg) {
    ApiResponse<T> r = new ApiResponse<>();
    r.code = -1;
    r.message = msg;
    return r;
  }
}