package com.baize.sb.product;

import com.baize.sb.product.vo.ProductSummaryResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, UUID> {

  @Query("""
      select distinct p
      from Product p
      left join fetch p.skus
      where p.id = :id
      """)
  Optional<Product> findDetailsById(@Param("id") UUID id);

  @Query(
      value = """
          select new com.baize.sb.product.vo.ProductSummaryResponse(
              p.id, p.name, p.coverUrl, min(s.price))
          from Product p
          join p.skus s
          where p.status = :productStatus
            and s.status = :skuStatus
            and (:keyword is null or lower(p.name) like lower(concat('%', :keyword, '%')))
          group by p.id, p.name, p.coverUrl, p.createdAt
          order by p.createdAt desc
          """,
      countQuery = """
          select count(distinct p.id)
          from Product p
          join p.skus s
          where p.status = :productStatus
            and s.status = :skuStatus
            and (:keyword is null or lower(p.name) like lower(concat('%', :keyword, '%')))
          """)
  Page<ProductSummaryResponse> findStorefront(
      @Param("productStatus") ProductStatus productStatus,
      @Param("skuStatus") SkuStatus skuStatus,
      @Param("keyword") String keyword,
      Pageable pageable);
}
