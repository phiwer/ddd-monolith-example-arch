package com.example.shared;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

/**
 * Shared Kernel — kept intentionally tiny.
 * Every type here is a coupling point between bounded contexts,
 * so only universally agreed-upon value objects belong here.
 */
@Embeddable
public record ProductId(String value) implements Serializable {

    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId must not be blank");
        }
    }
}
