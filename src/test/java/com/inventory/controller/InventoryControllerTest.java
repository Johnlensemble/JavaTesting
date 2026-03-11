package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Item createItem(String name, int qty, String price) throws Exception {
        Item item = new Item(null, name, qty, new BigDecimal(price));
        MvcResult result = mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), Item.class);
    }

    @Test
    void postItem_returns201() throws Exception {
        createItem("Widget", 10, "9.99");
    }

    @Test
    void getById_returns200() throws Exception {
        Item created = createItem("Gadget", 5, "4.50");

        mockMvc.perform(get("/api/items/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gadget"));
    }

    @Test
    void getById_returns404ForMissing() throws Exception {
        mockMvc.perform(get("/api/items/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_returns200() throws Exception {
        createItem("Item1", 1, "1.00");

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void putItem_returns200() throws Exception {
        Item created = createItem("Old", 1, "1.00");
        Item updated = new Item(null, "New", 99, new BigDecimal("5.00"));

        mockMvc.perform(put("/api/items/" + created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.quantity").value(99));
    }

    @Test
    void putItem_returns404ForMissing() throws Exception {
        mockMvc.perform(put("/api/items/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X\",\"quantity\":1,\"price\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteItem_returns204() throws Exception {
        Item created = createItem("ToDelete", 1, "1.00");

        mockMvc.perform(delete("/api/items/" + created.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteItem_returns404ForMissing() throws Exception {
        mockMvc.perform(delete("/api/items/999999"))
                .andExpect(status().isNotFound());
    }
}
