package com.baize.sb.product.dto;

import com.baize.sb.product.SkuStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class UpdateSkuRequest {

  @Size(max = 64, message = "SKU 编码不能超过 64 个字符")
  private String skuCode;

  private Map<String, String> attributes;

  @DecimalMin(value = "0.00", message = "SKU 价格不能小于 0")
  private BigDecimal price;

  @Min(value = 0, message = "SKU 库存不能小于 0")
  private Integer stock;

  private SkuStatus status;

  public boolean hasUpdates() {
    return skuCode != null
        || attributes != null
        || price != null
        || stock != null
        || status != null;
  }
}
