package com.baize.sb;

import static org.assertj.core.api.Assertions.assertThat;

import com.baize.sb.cart.CartItem;
import com.baize.sb.cart.CartItemRepository;
import com.baize.sb.product.Product;
import com.baize.sb.product.ProductRepository;
import com.baize.sb.product.ProductSku;
import com.baize.sb.product.ProductStatus;
import com.baize.sb.product.SkuStatus;
import com.baize.sb.product.ProductService;
import com.baize.sb.product.dto.UpdateSkuRequest;
import com.baize.sb.product.vo.ProductSummaryResponse;
import com.baize.sb.user.User;
import com.baize.sb.user.UserRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommerceRepositoryTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private ProductService productService;

  @Autowired
  private EntityManager entityManager;

  @Test
  void cartDetailsLoadsSkuAndProductWithOneRepositoryQuery() {
    User user = createUser();
    Product product = createProduct("JOIN-" + UUID.randomUUID());
    ProductSku sku = product.getSkus().get(0);

    CartItem item = new CartItem();
    item.setUser(user);
    item.setSku(sku);
    item.setQuantity(2);
    cartItemRepository.saveAndFlush(item);
    entityManager.clear();

    CartItem loaded = cartItemRepository.findCartDetails(user.getId()).get(0);

    assertThat(Hibernate.isInitialized(loaded.getSku())).isTrue();
    assertThat(Hibernate.isInitialized(loaded.getSku().getProduct())).isTrue();
    assertThat(loaded.getSku().getProduct().getName()).isEqualTo("Repository test product");
    assertThat(loaded.getSku().getPrice()).isEqualByComparingTo("19.90");

    User otherUser = createUser();
    assertThat(cartItemRepository.findCartDetails(otherUser.getId())).isEmpty();
    assertThat(cartItemRepository.findDetailsByIdAndUserId(item.getId(), otherUser.getId())).isEmpty();
  }

  @Test
  void storefrontQueryReturnsMinimumEnabledSkuPrice() {
    Product product = createProduct("LIST-" + UUID.randomUUID());
    ProductSku secondSku = new ProductSku();
    secondSku.setSkuCode("LIST-SECOND-" + UUID.randomUUID());
    secondSku.setAttributes(Map.of("size", "L"));
    secondSku.setPrice(new BigDecimal("15.50"));
    secondSku.setStock(5);
    secondSku.setStatus(SkuStatus.ENABLED);
    product.addSku(secondSku);
    productRepository.saveAndFlush(product);

    Page<ProductSummaryResponse> page = productRepository.findStorefront(
        ProductStatus.ON_SALE,
        SkuStatus.ENABLED,
        "Repository test",
        PageRequest.of(0, 10));

    assertThat(page.getContent()).hasSize(1);
    assertThat(page.getContent().get(0).minPrice()).isEqualByComparingTo("15.50");
  }

  @Test
  void partialSkuUpdateKeepsFieldsThatWereNotProvided() {
    Product product = createProduct("PATCH-" + UUID.randomUUID());
    ProductSku sku = product.getSkus().get(0);
    UpdateSkuRequest request = new UpdateSkuRequest();
    request.setStock(3);

    productService.updateSku(product.getId(), sku.getId(), request);
    productRepository.flush();
    entityManager.clear();

    Product updated = productRepository.findDetailsById(product.getId()).orElseThrow();
    ProductSku updatedSku = updated.getSkus().get(0);
    assertThat(updatedSku.getStock()).isEqualTo(3);
    assertThat(updatedSku.getSkuCode()).isEqualTo(sku.getSkuCode());
    assertThat(updatedSku.getPrice()).isEqualByComparingTo("19.90");
    assertThat(updatedSku.getAttributes()).containsEntry("color", "black");
    assertThat(updatedSku.getStatus()).isEqualTo(SkuStatus.ENABLED);
  }

  private User createUser() {
    User user = new User();
    user.setName("repository-test-" + UUID.randomUUID());
    user.setAge(20);
    user.setPassword("encoded-password");
    return userRepository.saveAndFlush(user);
  }

  private Product createProduct(String skuCode) {
    Product product = new Product();
    product.setName("Repository test product");
    product.setDescription("Repository integration test");
    product.setStatus(ProductStatus.ON_SALE);

    ProductSku sku = new ProductSku();
    sku.setSkuCode(skuCode);
    sku.setAttributes(Map.of("color", "black"));
    sku.setPrice(new BigDecimal("19.90"));
    sku.setStock(10);
    sku.setStatus(SkuStatus.ENABLED);
    product.addSku(sku);

    return productRepository.saveAndFlush(product);
  }
}
