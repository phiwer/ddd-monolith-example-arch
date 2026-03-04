/**
 * Order aggregate — the Ordering context's core domain concept.
 *
 * An Order contains OrderLines that reference products by ID only,
 * with a snapshot of the unit price at time of ordering. The Order
 * doesn't know what a product looks like — it speaks in quantities,
 * prices, and fulfillment status.
 *
 * Aggregate root: {@link com.example.ordering.domain.order.Order}
 */
package com.example.ordering.domain.order;
