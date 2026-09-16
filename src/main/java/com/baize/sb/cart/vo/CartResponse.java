package com.baize.sb.cart.vo;

import java.util.List;

public record CartResponse(
    List<CartItemResponse> items,
    int totalQuantity) {
}
