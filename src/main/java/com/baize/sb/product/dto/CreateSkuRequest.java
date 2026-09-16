package com.baize.sb.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CreateSkuRequest {

  @NotBlank(message = "SKU 编码不能为空")
  @Size(max = 64, message = "SKU 编码不能超过 64 个字符")
  private String skuCode;

  @NotNull(message = "SKU 规格不能为空")
  private Map<String, String> attributes = new LinkedHashMap<>();

  @NotNull(message = "SKU 价格不能为空")
  @DecimalMin(value = "0.00", message = "SKU 价格不能小于 0")
  private BigDecimal price;

  @NotNull(message = "SKU 库存不能为空")
  @Min(value = 0, message = "SKU 库存不能小于 0")
  private Integer stock;
}
