package com.baize.sb.product;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductSkuRepository extends JpaRepository<ProductSku, UUID> {

  boolean existsBySkuCode(String skuCode);

  boolean existsBySkuCodeAndIdNot(String skuCode, UUID id);

  Optional<ProductSku> findByIdAndProductId(UUID id, UUID productId);

  @Query("""
      select s
      from ProductSku s
      join fetch s.product
      where s.id = :id
      """)
  Optional<ProductSku> findWithProductById(@Param("id") UUID id);
}
