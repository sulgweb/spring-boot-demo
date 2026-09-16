package com.baize.sb.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", format = "int32", allowableValues = {"0", "1"},
    description = "SKU 状态：0-禁用，1-启用")
public enum SkuStatus {
  DISABLED(0),
  ENABLED(1);

  private final int code;

  SkuStatus(int code) {
    this.code = code;
  }

  @JsonValue
  public int getCode() {
    return code;
  }

  @JsonCreator
  public static SkuStatus fromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (SkuStatus status : values()) {
      if (status.code == code) {
        return status;
      }
    }
    throw new IllegalArgumentException("未知 SKU 状态码: " + code);
  }
}
