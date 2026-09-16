package com.baize.sb.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "integer", format = "int32", allowableValues = {"0", "1", "2"},
    description = "商品状态：0-草稿，1-上架，2-下架")
public enum ProductStatus {
  DRAFT(0),
  ON_SALE(1),
  OFF_SALE(2);

  private final int code;

  ProductStatus(int code) {
    this.code = code;
  }

  @JsonValue
  public int getCode() {
    return code;
  }

  @JsonCreator
  public static ProductStatus fromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (ProductStatus status : values()) {
      if (status.code == code) {
        return status;
      }
    }
    throw new IllegalArgumentException("未知商品状态码: " + code);
  }
}
