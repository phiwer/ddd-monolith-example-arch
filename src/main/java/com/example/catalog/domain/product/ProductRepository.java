package com.example.catalog.domain.product;

import com.example.shared.ProductId;

import java.util.Optional;

/**
 * Repository contract defined by the domain.
 * Infrastructure provides the implementation — the domain doesn't know or care how.
 */
public interface ProductRepository {

    Optional<Product> findById(ProductId id);

    void save(Product product);
}
