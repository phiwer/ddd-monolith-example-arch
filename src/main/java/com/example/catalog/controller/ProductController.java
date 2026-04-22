package com.example.catalog.controller;

import com.example.catalog.application.CatalogService;
import com.example.catalog.application.data.CreateProductCommand;
import com.example.catalog.application.data.ProductView;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inbound adapter for HTTP. Translates requests into calls on the
 * CatalogService port and maps results back to HTTP responses.
 *
 * Lives alongside application/, domain/, infrastructure/ — it is the
 * driving side of ports-and-adapters, not part of the domain.
 */
@RestController
@RequestMapping("/products")
class ProductController {

    private final CatalogService catalogService;

    ProductController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping
    ResponseEntity<ProductView> create(@RequestBody CreateProductCommand command) {
        ProductView created = catalogService.createProduct(command);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductView> findById(@PathVariable String id) {
        return catalogService.findProduct(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/publish")
    ResponseEntity<Void> publish(@PathVariable String id) {
        try {
            catalogService.publishProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
