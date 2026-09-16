package com.baize.sb.product.dto;

import com.baize.sb.product.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProductStatusRequest {

  @NotNull(message = "商品状态不能为空")
  private ProductStatus status;
}
