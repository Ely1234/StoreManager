package com.example.store.product;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('ADMIN')")
  public ProductDtos.ProductResponse create(@Valid @RequestBody ProductDtos.CreateProductRequest req) {
    return ProductDtos.ProductResponse.from(service.create(req));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ProductDtos.ProductResponse getById(@PathVariable UUID id) {
    return ProductDtos.ProductResponse.from(service.getById(id));
  }

  @GetMapping("/by-sku/{sku}")
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ProductDtos.ProductResponse getBySku(@PathVariable String sku) {
    return ProductDtos.ProductResponse.from(service.getBySku(sku));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public Page<ProductDtos.ProductResponse> list(Pageable pageable) {
    return service.list(pageable).map(ProductDtos.ProductResponse::from);
  }

  @PatchMapping("/{id}/price")
  @PreAuthorize("hasRole('ADMIN')")
  public ProductDtos.ProductResponse updatePrice(@PathVariable UUID id,
                                                @Valid @RequestBody ProductDtos.UpdatePriceRequest req) {
    return ProductDtos.ProductResponse.from(service.updatePrice(id, req.price()));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ADMIN')")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
