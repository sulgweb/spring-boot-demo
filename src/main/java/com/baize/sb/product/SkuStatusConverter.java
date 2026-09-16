package com.baize.sb.product;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SkuStatusConverter implements AttributeConverter<SkuStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(SkuStatus attribute) {
    return attribute == null ? null : attribute.getCode();
  }

  @Override
  public SkuStatus convertToEntityAttribute(Integer dbData) {
    return SkuStatus.fromCode(dbData);
  }
}
