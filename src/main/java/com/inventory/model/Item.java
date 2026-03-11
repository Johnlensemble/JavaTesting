package com.inventory.model;

import java.math.BigDecimal;

public record Item(Long id, String name, int quantity, BigDecimal price) {}
