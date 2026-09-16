package com.baize.sb.product;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ProductStatus attribute) {
    return attribute == null ? null : attribute.getCode();
  }

  @Override
  public ProductStatus convertToEntityAttribute(Integer dbData) {
    return ProductStatus.fromCode(dbData);
  }
}
