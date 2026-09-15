package com.baize.sb.common;

import lombok.Data;

@Data
public class ApiResponse<T> {
  private int code;
  private String message;
  private T data;

  public static <T> ApiResponse<T> ok(T data) {
    ApiResponse<T> r = new ApiResponse<>();
    r.setCode(0);
    r.setMessage("success");
    r.setData(data);
    return r;
  }

  public static <T> ApiResponse<T> fail(String msg) {
    ApiResponse<T> r = new ApiResponse<>();
    r.setCode(-1);
    r.setMessage(msg);
    return r;
  }
}
