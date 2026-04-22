package com.example.catalog.domain.product;

import com.example.shared.Money;
import com.example.shared.ProductId;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Product in the Catalog context: rich with descriptions, categories, pricing.
 * This is the aggregate root for the Catalog bounded context.
 *
 * Note how different this is from what "Product" means in Ordering or Inventory.
 */
@Entity
@Table(name = "products")
public class Product {

    @EmbeddedId
    private ProductId id;

    private String name;
    private String description;
    private String category;

    @Embedded
    private Money price;

    private boolean published;

    protected Product() {} // JPA

    public Product(ProductId id, String name, String description, String category, Money price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.published = false;
    }

    public void publish() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Cannot publish a product without a name");
        }
        this.published = true;
    }

    public ProductId id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public String category() { return category; }
    public Money price() { return price; }
    public boolean isPublished() { return published; }
}
