package com.inventory.service;

import com.inventory.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InventoryService {

    private final ConcurrentHashMap<Long, Item> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Item create(Item item) {
        long id = idGenerator.getAndIncrement();
        var saved = new Item(id, item.name(), item.quantity(), item.price());
        store.put(id, saved);
        return saved;
    }

    public Optional<Item> getById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Item> getAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Item> update(Long id, Item updated) {
        return Optional.ofNullable(store.computeIfPresent(id, (key, existing) ->
                new Item(key, updated.name(), updated.quantity(), updated.price())
        ));
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
