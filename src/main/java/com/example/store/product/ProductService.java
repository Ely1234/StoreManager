package com.example.store.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository repo;

  public ProductService(ProductRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public Product create(ProductDtos.CreateProductRequest req) {
    if (repo.existsBySku(req.sku())) {
      throw new DuplicateSkuException("Product with sku '%s' already exists".formatted(req.sku()));
    }

    Product p = new Product();
    p.setSku(req.sku());
    p.setName(req.name());
    p.setDescription(req.description());
    p.setPrice(req.price());
    p.setCurrency(req.currency());
    p.setQuantity(req.quantity());

    Product saved = repo.save(p);
    log.debug("Created product id={} sku={}", saved.getId(), saved.getSku());
    return saved;
  }

  @Transactional(readOnly = true)
  public Product getById(UUID id) {
    return repo.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with id '%s' was not found".formatted(id)));
  }

  @Transactional(readOnly = true)
  public Product getBySku(String sku) {
    return repo.findBySku(sku)
        .orElseThrow(() -> new ProductNotFoundException("Product with sku '%s' was not found".formatted(sku)));
  }

  @Transactional(readOnly = true)
  public Page<Product> list(Pageable pageable) {
    return repo.findAll(pageable);
  }

  @Transactional
  public Product updatePrice(UUID id, BigDecimal newPrice) {
    Product p = getById(id);
    p.setPrice(newPrice);
    Product saved = repo.save(p);
    log.debug("Updated price for product id={} newPrice={}", id, newPrice);
    return saved;
  }

  @Transactional
  public void delete(UUID id) {
    Product p = getById(id);
    repo.delete(p);
    log.debug("Deleted product id={}", id);
  }
}
