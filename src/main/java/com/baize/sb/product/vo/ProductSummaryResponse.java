package com.baize.sb.product.vo;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSummaryResponse(
    UUID id,
    String name,
    String coverUrl,
    BigDecimal minPrice) {
}
