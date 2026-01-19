package com.example.store.product;

public class DuplicateSkuException extends RuntimeException {
  public DuplicateSkuException(String message) {
    super(message);
  }
}
