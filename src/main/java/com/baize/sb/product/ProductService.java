package com.baize.sb.product;

import com.baize.sb.common.BusinessException;
import com.baize.sb.common.PageResponse;
import com.baize.sb.product.dto.CreateProductRequest;
import com.baize.sb.product.dto.CreateSkuRequest;
import com.baize.sb.product.dto.UpdateProductRequest;
import com.baize.sb.product.dto.UpdateSkuRequest;
import com.baize.sb.product.vo.ProductDetailResponse;
import com.baize.sb.product.vo.ProductSummaryResponse;
import com.baize.sb.product.vo.SkuResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductSkuRepository skuRepository;

  public ProductService(ProductRepository productRepository, ProductSkuRepository skuRepository) {
    this.productRepository = productRepository;
    this.skuRepository = skuRepository;
  }

  @Transactional(readOnly = true)
  public PageResponse<ProductSummaryResponse> listPublished(String keyword, int page, int size) {
    String normalizedKeyword = keyword == null || keyword.isBlank() ? null : keyword.trim();
    Page<ProductSummaryResponse> result = productRepository.findStorefront(
        ProductStatus.ON_SALE,
        SkuStatus.ENABLED,
        normalizedKeyword,
        PageRequest.of(page, size));
    return PageResponse.from(result);
  }

  @Transactional(readOnly = true)
  public ProductDetailResponse getPublished(UUID productId) {
    Product product = getProductDetails(productId);
    if (product.getStatus() != ProductStatus.ON_SALE) {
      throw new BusinessException("商品未上架");
    }
    return toResponse(product, true);
  }

  @Transactional
  public ProductDetailResponse create(CreateProductRequest request) {
    ensureSkuCodesUnique(request.getSkus());

    Product product = new Product();
    product.setName(request.getName().trim());
    product.setDescription(request.getDescription());
    product.setCoverUrl(request.getCoverUrl());
    product.setStatus(ProductStatus.DRAFT);

    for (CreateSkuRequest skuRequest : request.getSkus()) {
      product.addSku(buildSku(skuRequest));
    }

    return toResponse(productRepository.save(product), false);
  }

  @Transactional
  public ProductDetailResponse update(UUID productId, UpdateProductRequest request) {
    Product product = getProductDetails(productId);
    product.setName(request.getName().trim());
    product.setDescription(request.getDescription());
    product.setCoverUrl(request.getCoverUrl());
    return toResponse(product, false);
  }

  @Transactional
  public ProductDetailResponse changeStatus(UUID productId, ProductStatus status) {
    Product product = getProductDetails(productId);
    if (status == ProductStatus.ON_SALE
        && product.getSkus().stream().noneMatch(sku -> sku.getStatus() == SkuStatus.ENABLED)) {
      throw new BusinessException("至少需要一个启用的 SKU 才能上架");
    }
    product.setStatus(status);
    return toResponse(product, false);
  }

  @Transactional
  public ProductDetailResponse addSku(UUID productId, CreateSkuRequest request) {
    Product product = getProductDetails(productId);
    String skuCode = request.getSkuCode().trim();
    if (skuRepository.existsBySkuCode(skuCode)) {
      throw new BusinessException("SKU 编码已存在");
    }
    product.addSku(buildSku(request));
    return toResponse(product, false);
  }

  @Transactional
  public ProductDetailResponse updateSku(UUID productId, UUID skuId, UpdateSkuRequest request) {
    if (!request.hasUpdates()) {
      throw new BusinessException("至少需要提供一个要修改的字段");
    }

    ProductSku sku = skuRepository.findByIdAndProductId(skuId, productId)
        .orElseThrow(() -> new BusinessException("SKU 不存在"));
    if (request.getSkuCode() != null) {
      String skuCode = request.getSkuCode().trim();
      if (skuCode.isEmpty()) {
        throw new BusinessException("SKU 编码不能为空");
      }
      if (skuRepository.existsBySkuCodeAndIdNot(skuCode, skuId)) {
        throw new BusinessException("SKU 编码已存在");
      }
      sku.setSkuCode(skuCode);
    }
    if (request.getAttributes() != null) {
      sku.setAttributes(request.getAttributes());
    }
    if (request.getPrice() != null) {
      sku.setPrice(request.getPrice());
    }
    if (request.getStock() != null) {
      sku.setStock(request.getStock());
    }
    if (request.getStatus() != null) {
      sku.setStatus(request.getStatus());
    }

    Product product = getProductDetails(productId);
    if (product.getStatus() == ProductStatus.ON_SALE
        && product.getSkus().stream().noneMatch(item -> item.getStatus() == SkuStatus.ENABLED)) {
      throw new BusinessException("上架商品至少需要一个启用的 SKU");
    }
    return toResponse(product, false);
  }

  private Product getProductDetails(UUID productId) {
    return productRepository.findDetailsById(productId)
        .orElseThrow(() -> new BusinessException("商品不存在"));
  }

  private ProductSku buildSku(CreateSkuRequest request) {
    String skuCode = request.getSkuCode().trim();
    if (skuRepository.existsBySkuCode(skuCode)) {
      throw new BusinessException("SKU 编码已存在");
    }
    ProductSku sku = new ProductSku();
    sku.setSkuCode(skuCode);
    sku.setAttributes(request.getAttributes());
    sku.setPrice(request.getPrice());
    sku.setStock(request.getStock());
    sku.setStatus(SkuStatus.ENABLED);
    return sku;
  }

  private void ensureSkuCodesUnique(List<CreateSkuRequest> requests) {
    Set<String> skuCodes = new HashSet<>();
    for (CreateSkuRequest request : requests) {
      if (!skuCodes.add(request.getSkuCode().trim())) {
        throw new BusinessException("请求中存在重复的 SKU 编码");
      }
    }
  }

  private ProductDetailResponse toResponse(Product product, boolean onlyEnabledSkus) {
    List<SkuResponse> skus = product.getSkus().stream()
        .filter(sku -> !onlyEnabledSkus || sku.getStatus() == SkuStatus.ENABLED)
        .map(SkuResponse::from)
        .toList();
    return new ProductDetailResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getCoverUrl(),
        product.getStatus(),
        skus);
  }
}
