package com.baize.sb.product.vo;

import com.baize.sb.product.ProductSku;
import com.baize.sb.product.SkuStatus;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record SkuResponse(
    UUID id,
    String skuCode,
    Map<String, String> attributes,
    BigDecimal price,
    Integer stock,
    SkuStatus status) {

  public static SkuResponse from(ProductSku sku) {
    return new SkuResponse(
        sku.getId(),
        sku.getSkuCode(),
        sku.getAttributes(),
        sku.getPrice(),
        sku.getStock(),
        sku.getStatus());
  }
}
