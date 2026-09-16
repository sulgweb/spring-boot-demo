package com.baize.sb.cart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class AddCartItemRequest {

  @NotNull(message = "SKU 不能为空")
  private UUID skuId;

  @NotNull(message = "购买数量不能为空")
  @Min(value = 1, message = "购买数量至少为 1")
  @Max(value = 99, message = "单个 SKU 最多购买 99 件")
  private Integer quantity;
}
