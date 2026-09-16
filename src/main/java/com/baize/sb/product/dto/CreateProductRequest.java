package com.baize.sb.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreateProductRequest {

  @NotBlank(message = "商品名称不能为空")
  @Size(max = 120, message = "商品名称不能超过 120 个字符")
  private String name;

  private String description;

  @Size(max = 500, message = "商品图片地址不能超过 500 个字符")
  private String coverUrl;

  @Valid
  @NotEmpty(message = "商品至少需要一个 SKU")
  private List<CreateSkuRequest> skus = new ArrayList<>();
}
