package com.example.shared;

/**
 * Shared Kernel — kept intentionally tiny.
 * Every type here is a coupling point between bounded contexts,
 * so only universally agreed-upon value objects belong here.
 */
public record ProductId(String value) {

    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId must not be blank");
        }
    }
}
