package com.baize.sb.cart.vo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record CartItemResponse(
    UUID id,
    UUID productId,
    String productName,
    String coverUrl,
    UUID skuId,
    String skuCode,
    Map<String, String> attributes,
    BigDecimal unitPrice,
    Integer stock,
    Integer quantity,
    boolean available,
    String invalidReason,
    BigDecimal subtotal) {
}
