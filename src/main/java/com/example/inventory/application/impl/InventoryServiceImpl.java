package com.example.inventory.application.impl;

import com.example.inventory.application.data.RegisterStockCommand;
import com.example.inventory.application.data.ReserveStockCommand;
import com.example.inventory.application.data.StockView;
import com.example.inventory.application.InventoryService;
import com.example.inventory.domain.stock.StockItem;
import com.example.inventory.domain.stock.StockItemRepository;
import com.example.shared.ProductId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
class InventoryServiceImpl implements InventoryService {

    private final StockItemRepository stockItemRepository;

    InventoryServiceImpl(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    @Override
    public StockView registerStock(RegisterStockCommand command) {
        var stockItem = new StockItem(
                new ProductId(command.productId()),
                command.initialQuantity(),
                command.reorderThreshold()
        );

        stockItemRepository.save(stockItem);
        return toView(stockItem);
    }

    @Override
    public void reserveStock(ReserveStockCommand command) {
        var stockItem = stockItemRepository.findByProductId(new ProductId(command.productId()))
                .orElseThrow(() -> new IllegalArgumentException("No stock registered for: " + command.productId()));

        stockItem.reserve(command.quantity());
        stockItemRepository.save(stockItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockView> findStock(String productId) {
        return stockItemRepository.findByProductId(new ProductId(productId))
                .map(this::toView);
    }

    private StockView toView(StockItem item) {
        return new StockView(
                item.productId().value(),
                item.quantityOnHand(),
                item.needsReorder()
        );
    }
}
