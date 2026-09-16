package com.baize.sb.cart;

import com.baize.sb.cart.dto.AddCartItemRequest;
import com.baize.sb.cart.dto.UpdateCartItemRequest;
import com.baize.sb.cart.vo.CartResponse;
import com.baize.sb.common.ApiResponse;
import com.baize.sb.config.OpenApiConfig;
import com.baize.sb.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class CartController {

  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping
  public ApiResponse<CartResponse> getCart(@AuthenticationPrincipal AuthenticatedUser user) {
    return ApiResponse.ok(cartService.getCart(user.id()));
  }

  @PostMapping("/items")
  public ApiResponse<CartResponse> addItem(
      @AuthenticationPrincipal AuthenticatedUser user,
      @Valid @RequestBody AddCartItemRequest request) {
    return ApiResponse.ok(cartService.addItem(user.id(), request));
  }

  @PatchMapping("/items/{itemId}")
  public ApiResponse<CartResponse> updateItem(
      @AuthenticationPrincipal AuthenticatedUser user,
      @PathVariable UUID itemId,
      @Valid @RequestBody UpdateCartItemRequest request) {
    return ApiResponse.ok(cartService.updateItem(user.id(), itemId, request));
  }

  @DeleteMapping("/items/{itemId}")
  public ApiResponse<CartResponse> removeItem(
      @AuthenticationPrincipal AuthenticatedUser user,
      @PathVariable UUID itemId) {
    return ApiResponse.ok(cartService.removeItem(user.id(), itemId));
  }

}
