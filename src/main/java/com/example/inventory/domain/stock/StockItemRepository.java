package com.example.inventory.domain.stock;

import com.example.shared.ProductId;

import java.util.Optional;

public interface StockItemRepository {

    Optional<StockItem> findByProductId(ProductId productId);

    void save(StockItem stockItem);
}
