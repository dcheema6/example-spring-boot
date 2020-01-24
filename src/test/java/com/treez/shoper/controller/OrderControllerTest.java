package com.treez.shoper.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import com.treez.shoper.AbstractTest;
import com.treez.shoper.model.Inventory;
import com.treez.shoper.model.Order;

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
public class OrderControllerTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();

        EntityManager em = emf.createEntityManager();
        Order o = new Order();
        Inventory i = new Inventory();
        i.setQuantity(10);

        em.getTransaction().begin();
        em.persist(o);
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
    public void createOrder() throws Exception {
        String uri = "/orders";
        Order order = new Order();
        order.setCustomerEmail("Ginger");

        String inputJson = super.mapToJson(order);
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.CREATED.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"status\":\"created\""));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void getOrderList() throws Exception {
        String uri = "/orders";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        Order[] Orderlist = super.mapFromJson(content, Order[].class);
        assertTrue(Orderlist.length > 0);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void getOrderById() throws Exception {
        String uri = "/orders/13";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"status\":\"created\""));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void updateOrder() throws Exception {
        String uri = "/orders/9";
        Order order = new Order();
        order.setStatus(Order.Status.canceled.toString());

        String inputJson = super.mapToJson(order);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(inputJson)).andReturn();
        
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"status\":\"canceled\""));
    }
    
    @Test
    @org.junit.jupiter.api.Order(5)
    public void deleteOrder() throws Exception {
        String uri = "/orders/7";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.NO_CONTENT.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("", content);
    }
}
