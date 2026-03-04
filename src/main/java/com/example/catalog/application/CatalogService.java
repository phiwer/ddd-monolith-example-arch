package com.example.catalog.application;

import com.example.catalog.application.data.CreateProductCommand;
import com.example.catalog.application.data.ProductView;

import java.util.Optional;

/**
 * The public interface of the Catalog bounded context.
 *
 * This is what other contexts (Ordering, Inventory) depend on.
 * They see this interface and the DTOs — nothing else.
 * They never touch Product.java, ProductRepository, or anything in domain/infrastructure.
 */
public interface CatalogService {

    ProductView createProduct(CreateProductCommand command);

    Optional<ProductView> findProduct(String productId);

    void publishProduct(String productId);
}
