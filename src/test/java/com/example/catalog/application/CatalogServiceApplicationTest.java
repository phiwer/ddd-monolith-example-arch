package com.example.catalog.application;

import com.example.TestApplication;
import com.example.catalog.application.data.CreateProductCommand;
import com.example.catalog.application.data.ProductView;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
class CatalogServiceApplicationTest {

    @Autowired
    CatalogService catalogService;

    @Test
    void createProduct_persistsAndCanBeRetrieved() {
        // Arrange
        String id = UUID.randomUUID().toString();
        CreateProductCommand command = new CreateProductCommand(
                id, "Widget", "A widget", "tools", new BigDecimal("9.99"), "SEK");

        // Act
        ProductView created = catalogService.createProduct(command);
        Optional<ProductView> found = catalogService.findProduct(id);

        // Assert
        assertThat(created.productId()).isEqualTo(id);
        assertThat(found).hasValueSatisfying(view -> {
            assertThat(view.name()).isEqualTo("Widget");
            assertThat(view.price()).isEqualByComparingTo("9.99");
            assertThat(view.currencyCode()).isEqualTo("SEK");
        });
    }

    @Test
    void findProduct_returnsEmpty_whenUnknown() {
        // Arrange
        String unknownId = "does-not-exist";

        // Act
        Optional<ProductView> result = catalogService.findProduct(unknownId);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void publishProduct_throws_whenUnknown() {
        // Arrange
        String unknownId = "does-not-exist";

        // Act / Assert
        assertThatThrownBy(() -> catalogService.publishProduct(unknownId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
