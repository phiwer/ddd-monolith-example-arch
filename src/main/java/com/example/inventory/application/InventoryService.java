package com.example.inventory.application;

import com.example.inventory.application.data.RegisterStockCommand;
import com.example.inventory.application.data.ReserveStockCommand;
import com.example.inventory.application.data.StockView;

import java.util.Optional;

public interface InventoryService {

    StockView registerStock(RegisterStockCommand command);

    void reserveStock(ReserveStockCommand command);

    Optional<StockView> findStock(String productId);
}
