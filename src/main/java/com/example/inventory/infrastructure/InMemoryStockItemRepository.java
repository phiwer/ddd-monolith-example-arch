package com.example.inventory.infrastructure;

import com.example.inventory.domain.stock.StockItem;
import com.example.inventory.domain.stock.StockItemRepository;
import com.example.shared.ProductId;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class InMemoryStockItemRepository implements StockItemRepository {

    private final Map<ProductId, StockItem> store = new ConcurrentHashMap<>();

    @Override
    public Optional<StockItem> findByProductId(ProductId productId) {
        return Optional.ofNullable(store.get(productId));
    }

    @Override
    public void save(StockItem stockItem) {
        store.put(stockItem.productId(), stockItem);
    }
}
