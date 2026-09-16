package com.baize.sb.cart;

import com.baize.sb.cart.dto.AddCartItemRequest;
import com.baize.sb.cart.dto.UpdateCartItemRequest;
import com.baize.sb.cart.vo.CartItemResponse;
import com.baize.sb.cart.vo.CartResponse;
import com.baize.sb.common.BusinessException;
import com.baize.sb.product.Product;
import com.baize.sb.product.ProductSku;
import com.baize.sb.product.ProductSkuRepository;
import com.baize.sb.product.ProductStatus;
import com.baize.sb.product.SkuStatus;
import com.baize.sb.user.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

  private static final int MAX_QUANTITY_PER_SKU = 99;

  private final CartItemRepository cartItemRepository;
  private final ProductSkuRepository skuRepository;
  private final UserRepository userRepository;

  public CartService(
      CartItemRepository cartItemRepository,
      ProductSkuRepository skuRepository,
      UserRepository userRepository) {
    this.cartItemRepository = cartItemRepository;
    this.skuRepository = skuRepository;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public CartResponse getCart(UUID userId) {
    return buildCartResponse(cartItemRepository.findCartDetails(userId));
  }

  @Transactional
  public CartResponse addItem(UUID userId, AddCartItemRequest request) {
    ProductSku sku = skuRepository.findWithProductById(request.getSkuId())
        .orElseThrow(() -> new BusinessException("SKU 不存在"));

    CartItem item = cartItemRepository.findForUpdate(userId, request.getSkuId()).orElse(null);
    int newQuantity = request.getQuantity() + (item == null ? 0 : item.getQuantity());
    validatePurchasable(sku, newQuantity);

    if (item == null) {
      item = new CartItem();
      item.setUser(userRepository.getReferenceById(userId));
      item.setSku(sku);
      item.setQuantity(request.getQuantity());
    } else {
      item.setQuantity(newQuantity);
    }
    cartItemRepository.save(item);
    return buildCartResponse(cartItemRepository.findCartDetails(userId));
  }

  @Transactional
  public CartResponse updateItem(UUID userId, UUID itemId, UpdateCartItemRequest request) {
    CartItem item = cartItemRepository.findDetailsByIdAndUserId(itemId, userId)
        .orElseThrow(() -> new BusinessException("购物车条目不存在"));
    validatePurchasable(item.getSku(), request.getQuantity());
    item.setQuantity(request.getQuantity());
    return buildCartResponse(cartItemRepository.findCartDetails(userId));
  }

  @Transactional
  public CartResponse removeItem(UUID userId, UUID itemId) {
    CartItem item = cartItemRepository.findDetailsByIdAndUserId(itemId, userId)
        .orElseThrow(() -> new BusinessException("购物车条目不存在"));
    cartItemRepository.delete(item);
    cartItemRepository.flush();
    return buildCartResponse(cartItemRepository.findCartDetails(userId));
  }

  private void validatePurchasable(ProductSku sku, int quantity) {
    if (sku.getProduct().getStatus() != ProductStatus.ON_SALE) {
      throw new BusinessException("商品已下架");
    }
    if (sku.getStatus() != SkuStatus.ENABLED) {
      throw new BusinessException("SKU 已停用");
    }
    if (quantity > MAX_QUANTITY_PER_SKU) {
      throw new BusinessException("单个 SKU 最多购买 " + MAX_QUANTITY_PER_SKU + " 件");
    }
    if (quantity > sku.getStock()) {
      throw new BusinessException("库存不足，当前仅剩 " + sku.getStock() + " 件");
    }
  }

  private CartResponse buildCartResponse(List<CartItem> cartItems) {
    List<CartItemResponse> responses = new ArrayList<>();
    int totalQuantity = 0;

    for (CartItem item : cartItems) {
      ProductSku sku = item.getSku();
      Product product = sku.getProduct();
      String invalidReason = getInvalidReason(item);
      boolean available = invalidReason == null;
      BigDecimal subtotal = sku.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

      responses.add(new CartItemResponse(
          item.getId(),
          product.getId(),
          product.getName(),
          product.getCoverUrl(),
          sku.getId(),
          sku.getSkuCode(),
          sku.getAttributes(),
          sku.getPrice(),
          sku.getStock(),
          item.getQuantity(),
          available,
          invalidReason,
          subtotal));

      totalQuantity += item.getQuantity();
    }

    return new CartResponse(responses, totalQuantity);
  }

  private String getInvalidReason(CartItem item) {
    ProductSku sku = item.getSku();
    if (sku.getProduct().getStatus() != ProductStatus.ON_SALE) {
      return "商品已下架";
    }
    if (sku.getStatus() != SkuStatus.ENABLED) {
      return "SKU 已停用";
    }
    if (sku.getStock() < item.getQuantity()) {
      return "库存不足，当前仅剩 " + sku.getStock() + " 件";
    }
    return null;
  }
}
