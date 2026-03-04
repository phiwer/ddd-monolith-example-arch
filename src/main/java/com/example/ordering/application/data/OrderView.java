package com.example.ordering.application.data;

import java.util.List;

public record OrderView(String orderId, String status, List<OrderLineView> lines) {}
