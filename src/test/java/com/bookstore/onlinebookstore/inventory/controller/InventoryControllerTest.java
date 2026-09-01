package com.bookstore.onlinebookstore.inventory.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bookstore.onlinebookstore.inventory.dto.InventoryRequest;
import com.bookstore.onlinebookstore.inventory.dto.InventoryResponse;
import com.bookstore.onlinebookstore.inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryRequest inventoryRequest;
    private InventoryResponse inventoryResponse;

    @BeforeEach
    void setUp() {

        inventoryRequest = new InventoryRequest(
                10L,
                50
        );

        inventoryResponse = new InventoryResponse(
                1L,
                10L,
                50
        );
    }

    @Test
    void createInventory_shouldReturnCreated() throws Exception {

        when(inventoryService.createInventory(inventoryRequest))
                .thenReturn(inventoryResponse);

        mockMvc.perform(
                post("/api/inventory")
                        .contentType("application/json")
                        .content(
                                objectMapper.writeValueAsString(
                                        inventoryRequest
                                )
                        )
        )
        .andExpect(status().isCreated());

        verify(inventoryService)
                .createInventory(inventoryRequest);
    }

    @Test
    void getInventory_shouldReturnInventory() throws Exception {

        when(inventoryService.getInventoryByBookId(10L))
                .thenReturn(inventoryResponse);

        mockMvc.perform(
                get("/api/inventory/10")
        )
        .andExpect(status().isOk());

        verify(inventoryService)
                .getInventoryByBookId(10L);
    }

    @Test
    void updateInventory_shouldReturnUpdatedInventory() throws Exception {

        var updateRequest = new InventoryRequest(
                10L,
                100
        );

        var updatedResponse = new InventoryResponse(
                1L,
                10L,
                100
        );

        when(inventoryService.updateInventory(
                10L,
                updateRequest
        )).thenReturn(updatedResponse);

        mockMvc.perform(
                put("/api/inventory/10")
                        .contentType("application/json")
                        .content(
                                objectMapper.writeValueAsString(
                                        updateRequest
                                )
                        )
        )
        .andExpect(status().isOk());

        verify(inventoryService)
                .updateInventory(10L, updateRequest);
    }
}