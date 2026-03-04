package com.example.shared;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Money requires amount and currency");
        }
    }

    public static Money sek(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("SEK"));
    }
}
