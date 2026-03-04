package com.example.inventory.domain.stock;

import com.example.shared.ProductId;

/**
 * In the Inventory context, we don't have "Products" — we have StockItems.
 * Same underlying thing, completely different model reflecting
 * what this context actually cares about.
 */
public class StockItem {

    private ProductId productId;
    private int quantityOnHand;
    private int reorderThreshold;

    protected StockItem() {} // JPA

    public StockItem(ProductId productId, int initialQuantity, int reorderThreshold) {
        this.productId = productId;
        this.quantityOnHand = initialQuantity;
        this.reorderThreshold = reorderThreshold;
    }

    public void reserve(int quantity) {
        if (quantity > quantityOnHand) {
            throw new IllegalStateException(
                    "Insufficient stock for %s: requested %d, available %d"
                            .formatted(productId.value(), quantity, quantityOnHand));
        }
        this.quantityOnHand -= quantity;
    }

    public void restock(int quantity) {
        this.quantityOnHand += quantity;
    }

    public boolean needsReorder() {
        return quantityOnHand <= reorderThreshold;
    }

    public ProductId productId() { return productId; }
    public int quantityOnHand() { return quantityOnHand; }
}
