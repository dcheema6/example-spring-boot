package com.treez.shoper.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import com.treez.shoper.AbstractTest;
import com.treez.shoper.model.Inventory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@TestMethodOrder(OrderAnnotation.class)
public class InventoryControllerTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();

        EntityManager em = emf.createEntityManager();
        Inventory i = new Inventory();
        i.setQuantity(10);
        
        em.getTransaction().begin();
        em.persist(i);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    public void createInventory() throws Exception {
        String uri = "/inventories";
        Inventory inventory = new Inventory();
        inventory.setQuantity(10);

        String inputJson = super.mapToJson(inventory);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.CREATED.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"quantity\":10"));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void getInventoryList() throws Exception {
        String uri = "/inventories";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        Inventory[] Inventorylist = super.mapFromJson(content, Inventory[].class);
        assertTrue(Inventorylist.length > 0);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void getInventoryById() throws Exception {
        String uri = "/inventories/2";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"quantity\":10"));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void updateInventory() throws Exception {
        String uri = "/inventories/2";
        Inventory inventory = new Inventory();
        inventory.setDescription("test");
        inventory.setQuantity(10);

        String inputJson = super.mapToJson(inventory);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(inputJson)).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"description\":\"test\""));
    }
    
    @Test
    @org.junit.jupiter.api.Order(5)
    public void deleteInventory() throws Exception {
        String uri = "/inventories/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.NO_CONTENT.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("", content);
    }
}
