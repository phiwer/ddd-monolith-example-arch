package com.example.ordering.application;

import com.example.TestApplication;
import com.example.catalog.application.CatalogService;
import com.example.catalog.application.data.CreateProductCommand;
import com.example.ordering.application.data.AddLineCommand;
import com.example.ordering.application.data.OrderLineView;
import com.example.ordering.application.data.OrderView;
import com.example.ordering.application.data.PlaceOrderCommand;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MOB SESSION STARTING POINT.
 *
 * Remove @Disabled below, run the test, watch it fail with
 * UnsupportedOperationException coming out of OrderingServiceImpl.placeOrder.
 *
 * The task: make it pass without touching Catalog's domain or infrastructure
 * packages (ArchUnit in BoundedContextBoundaryTest will catch you if you try).
 * Your only legal handle into Catalog is its application-layer port.
 */
@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
@Disabled("Mob session: enable once OrderingService talks to CatalogService through its application port")
class OrderingServiceApplicationTest {

    @Autowired
    OrderingService orderingService;

    @Autowired
    CatalogService catalogService;

    @Test
    void placeOrder_pullsUnitPriceFromCatalog() {
        // Arrange
        String productId = UUID.randomUUID().toString();
        catalogService.createProduct(new CreateProductCommand(
                productId, "Widget", "A widget", "tools", new BigDecimal("9.99"), "SEK"));
        PlaceOrderCommand command = new PlaceOrderCommand(List.of(
                new AddLineCommand(productId, 3)));

        // Act
        OrderView placed = orderingService.placeOrder(command);

        // Assert
        assertThat(placed.lines()).singleElement().satisfies(line -> {
            assertThat(line.productId()).isEqualTo(productId);
            assertThat(line.quantity()).isEqualTo(3);
            assertThat(line.unitPrice()).isEqualByComparingTo("9.99");
            assertThat(line.currencyCode()).isEqualTo("SEK");
        });
    }
}
