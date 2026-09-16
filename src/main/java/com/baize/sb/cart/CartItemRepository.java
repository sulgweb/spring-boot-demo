package com.baize.sb.cart;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

  @Query("""
      select ci
      from CartItem ci
      join fetch ci.sku sku
      join fetch sku.product product
      where ci.user.id = :userId
      order by ci.createdAt desc
      """)
  List<CartItem> findCartDetails(@Param("userId") UUID userId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      select ci
      from CartItem ci
      where ci.user.id = :userId and ci.sku.id = :skuId
      """)
  Optional<CartItem> findForUpdate(
      @Param("userId") UUID userId,
      @Param("skuId") UUID skuId);

  @Query("""
      select ci
      from CartItem ci
      join fetch ci.sku sku
      join fetch sku.product product
      where ci.id = :itemId and ci.user.id = :userId
      """)
  Optional<CartItem> findDetailsByIdAndUserId(
      @Param("itemId") UUID itemId,
      @Param("userId") UUID userId);

}
