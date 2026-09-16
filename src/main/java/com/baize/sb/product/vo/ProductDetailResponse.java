package com.baize.sb.product.vo;

import com.baize.sb.product.ProductStatus;
import java.util.List;
import java.util.UUID;

public record ProductDetailResponse(
    UUID id,
    String name,
    String description,
    String coverUrl,
    ProductStatus status,
    List<SkuResponse> skus) {
}
