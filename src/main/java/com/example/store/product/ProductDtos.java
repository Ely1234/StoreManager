package com.example.store.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class ProductDtos {
  private ProductDtos() {}

  public record CreateProductRequest(
      @NotBlank @Size(max = 64) String sku,
      @NotBlank @Size(max = 120) String name,
      @Size(max = 500) String description,
      @NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal price,
      @NotBlank @Pattern(regexp = "[A-Z]{3}", message = "must be a 3-letter ISO currency code") String currency,
      @NotNull @Min(0) Integer quantity
  ) {}

  public record UpdatePriceRequest(
      @NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal price
  ) {}

  public record ProductResponse(
      UUID id,
      String sku,
      String name,
      String description,
      BigDecimal price,
      String currency,
      Integer quantity,
      Instant createdAt,
      Instant updatedAt
  ) {
    public static ProductResponse from(Product p) {
      return new ProductResponse(
          p.getId(),
          p.getSku(),
          p.getName(),
          p.getDescription(),
          p.getPrice(),
          p.getCurrency(),
          p.getQuantity(),
          p.getCreatedAt(),
          p.getUpdatedAt()
      );
    }
  }
}
