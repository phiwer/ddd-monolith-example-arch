package com.example.ordering.application.data;

import java.util.List;

public record PlaceOrderCommand(List<AddLineCommand> lines) {}
