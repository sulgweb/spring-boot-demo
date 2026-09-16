package com.baize.sb.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {

  @NotBlank(message = "商品名称不能为空")
  @Size(max = 120, message = "商品名称不能超过 120 个字符")
  private String name;

  private String description;

  @Size(max = 500, message = "商品图片地址不能超过 500 个字符")
  private String coverUrl;
}
