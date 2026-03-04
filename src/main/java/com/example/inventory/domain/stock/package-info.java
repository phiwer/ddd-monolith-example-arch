/**
 * Stock aggregate — the Inventory context's core domain concept.
 *
 * In this context there are no "Products" — only StockItems tracked by
 * quantity on hand, reorder thresholds, and reservation logic.
 *
 * Aggregate root: {@link com.example.inventory.domain.stock.StockItem}
 */
package com.example.inventory.domain.stock;
