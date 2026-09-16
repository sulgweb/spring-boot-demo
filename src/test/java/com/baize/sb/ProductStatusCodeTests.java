package com.baize.sb;

import static org.assertj.core.api.Assertions.assertThat;

import com.baize.sb.product.ProductStatus;
import com.baize.sb.product.ProductStatusConverter;
import com.baize.sb.product.SkuStatus;
import com.baize.sb.product.SkuStatusConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ProductStatusCodeTests {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void productStatusUsesExplicitNumericCodes() throws Exception {
    ProductStatusConverter converter = new ProductStatusConverter();

    assertThat(converter.convertToDatabaseColumn(ProductStatus.DRAFT)).isZero();
    assertThat(converter.convertToDatabaseColumn(ProductStatus.ON_SALE)).isEqualTo(1);
    assertThat(converter.convertToDatabaseColumn(ProductStatus.OFF_SALE)).isEqualTo(2);
    assertThat(converter.convertToEntityAttribute(1)).isEqualTo(ProductStatus.ON_SALE);
    assertThat(objectMapper.writeValueAsString(ProductStatus.ON_SALE)).isEqualTo("1");
    assertThat(objectMapper.readValue("2", ProductStatus.class)).isEqualTo(ProductStatus.OFF_SALE);
  }

  @Test
  void skuStatusUsesExplicitNumericCodes() throws Exception {
    SkuStatusConverter converter = new SkuStatusConverter();

    assertThat(converter.convertToDatabaseColumn(SkuStatus.DISABLED)).isZero();
    assertThat(converter.convertToDatabaseColumn(SkuStatus.ENABLED)).isEqualTo(1);
    assertThat(converter.convertToEntityAttribute(0)).isEqualTo(SkuStatus.DISABLED);
    assertThat(objectMapper.writeValueAsString(SkuStatus.ENABLED)).isEqualTo("1");
    assertThat(objectMapper.readValue("0", SkuStatus.class)).isEqualTo(SkuStatus.DISABLED);
  }
}
