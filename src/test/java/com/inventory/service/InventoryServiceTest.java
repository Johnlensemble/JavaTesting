package com.inventory.service;

import com.inventory.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService();
    }

    @Test
    void create_assignsIdAndStoresItem() {
        Item item = new Item(null, "Widget", 10, new BigDecimal("9.99"));
        Item created = service.create(item);

        assertNotNull(created.id());
        assertEquals("Widget", created.name());
        assertEquals(10, created.quantity());
    }

    @Test
    void getById_returnsItem() {
        Item created = service.create(new Item(null, "Widget", 5, new BigDecimal("1.00")));

        Optional<Item> found = service.getById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Widget", found.get().name());
    }

    @Test
    void getById_returnsEmptyForMissingId() {
        assertFalse(service.getById(999L).isPresent());
    }

    @Test
    void getAll_returnsAllItems() {
        service.create(new Item(null, "A", 1, BigDecimal.ONE));
        service.create(new Item(null, "B", 2, BigDecimal.TEN));

        List<Item> all = service.getAll();

        assertEquals(2, all.size());
    }

    @Test
    void update_modifiesExistingItem() {
        Item created = service.create(new Item(null, "Old", 1, BigDecimal.ONE));
        Item updated = new Item(null, "New", 99, new BigDecimal("5.00"));

        Optional<Item> result = service.update(created.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals("New", result.get().name());
        assertEquals(99, result.get().quantity());
        assertEquals(new BigDecimal("5.00"), result.get().price());
    }

    @Test
    void update_returnsEmptyForMissingId() {
        assertFalse(service.update(999L, new Item(null, null, 0, null)).isPresent());
    }

    @Test
    void delete_removesItem() {
        Item created = service.create(new Item(null, "Widget", 1, BigDecimal.ONE));

        assertTrue(service.delete(created.id()));
        assertFalse(service.getById(created.id()).isPresent());
    }

    @Test
    void delete_returnsFalseForMissingId() {
        assertFalse(service.delete(999L));
    }
}
