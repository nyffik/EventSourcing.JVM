package io.eventdriven.uniqueness.shoppingcarts.productitems;

import java.util.UUID;

public record PricedProductItem(
  UUID productId,
  int quantity,
  double unitPrice
) {}
