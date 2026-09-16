package com.baize.sb.product;

import com.baize.sb.common.ApiResponse;
import com.baize.sb.config.OpenApiConfig;
import com.baize.sb.product.dto.CreateProductRequest;
import com.baize.sb.product.dto.CreateSkuRequest;
import com.baize.sb.product.dto.UpdateProductRequest;
import com.baize.sb.product.dto.UpdateProductStatusRequest;
import com.baize.sb.product.dto.UpdateSkuRequest;
import com.baize.sb.product.vo.ProductDetailResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/products")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class AdminProductController {

  private final ProductService productService;

  public AdminProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ApiResponse<ProductDetailResponse> create(@Valid @RequestBody CreateProductRequest request) {
    return ApiResponse.ok(productService.create(request));
  }

  @PutMapping("/{productId}")
  public ApiResponse<ProductDetailResponse> update(
      @PathVariable UUID productId,
      @Valid @RequestBody UpdateProductRequest request) {
    return ApiResponse.ok(productService.update(productId, request));
  }

  @PatchMapping("/{productId}/status")
  public ApiResponse<ProductDetailResponse> changeStatus(
      @PathVariable UUID productId,
      @Valid @RequestBody UpdateProductStatusRequest request) {
    return ApiResponse.ok(productService.changeStatus(productId, request.getStatus()));
  }

  @PostMapping("/{productId}/skus")
  public ApiResponse<ProductDetailResponse> addSku(
      @PathVariable UUID productId,
      @Valid @RequestBody CreateSkuRequest request) {
    return ApiResponse.ok(productService.addSku(productId, request));
  }

  @PatchMapping("/{productId}/skus/{skuId}")
  public ApiResponse<ProductDetailResponse> updateSku(
      @PathVariable UUID productId,
      @PathVariable UUID skuId,
      @Valid @RequestBody UpdateSkuRequest request) {
    return ApiResponse.ok(productService.updateSku(productId, skuId, request));
  }
}
