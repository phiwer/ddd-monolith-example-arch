package com.example.catalog.application.impl;

import com.example.catalog.application.data.CreateProductCommand;
import com.example.catalog.application.data.ProductView;
import com.example.catalog.application.CatalogService;
import com.example.catalog.domain.product.Product;
import com.example.catalog.domain.product.ProductRepository;
import com.example.shared.Money;
import com.example.shared.ProductId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.Optional;

@Service
@Transactional
class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;

    CatalogServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductView createProduct(CreateProductCommand command) {
        var product = new Product(
                new ProductId(command.productId()),
                command.name(),
                command.description(),
                command.category(),
                new Money(command.price(), Currency.getInstance(command.currencyCode()))
        );

        productRepository.save(product);
        return toView(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductView> findProduct(String productId) {
        return productRepository.findById(new ProductId(productId))
                .map(this::toView);
    }

    @Override
    public void publishProduct(String productId) {
        var product = productRepository.findById(new ProductId(productId))
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        product.publish();
        productRepository.save(product);
    }

    private ProductView toView(Product product) {
        return new ProductView(
                product.id().value(),
                product.name(),
                product.description(),
                product.category(),
                product.price().amount(),
                product.price().currency().getCurrencyCode()
        );
    }
}
