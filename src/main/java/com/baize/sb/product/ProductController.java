package com.baize.sb.product;

import com.baize.sb.common.ApiResponse;
import com.baize.sb.common.PageResponse;
import com.baize.sb.product.vo.ProductDetailResponse;
import com.baize.sb.product.vo.ProductSummaryResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ApiResponse<PageResponse<ProductSummaryResponse>> list(
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
    return ApiResponse.ok(productService.listPublished(keyword, page, size));
  }

  @GetMapping("/{productId}")
  public ApiResponse<ProductDetailResponse> detail(@PathVariable UUID productId) {
    return ApiResponse.ok(productService.getPublished(productId));
  }
}
