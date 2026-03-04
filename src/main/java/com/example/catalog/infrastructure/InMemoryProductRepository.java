package com.example.catalog.infrastructure;

import com.example.catalog.domain.product.Product;
import com.example.catalog.domain.product.ProductRepository;
import com.example.shared.ProductId;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In a real project this would wrap a Spring Data JPA repository.
 * Using an in-memory map here to keep the example self-contained.
 */
@Repository
class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Product> findById(ProductId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Product product) {
        store.put(product.id(), product);
    }
}
