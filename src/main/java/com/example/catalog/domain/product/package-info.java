/**
 * Product aggregate — the Catalog context's core domain concept.
 *
 * In this context a Product is rich: name, description, category, pricing, and
 * publication status. This is fundamentally different from how "product" is modeled
 * in the Ordering context (a line item) or Inventory context (a stock item).
 *
 * Aggregate root: {@link com.example.catalog.domain.product.Product}
 */
package com.example.catalog.domain.product;
