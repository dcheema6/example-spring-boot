package com.treez.shoper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.treez.shoper.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.treez.shoper.model.Order;;

@RestController
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            order = orderService.create(order);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (order == null) {
            return new ResponseEntity<Order>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<Order>(order, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/orders")
    public List<Order> getOrderList() {
        List<Order> tasks;
        
        try {
            tasks = orderService.getAsList();
        } catch (Exception e) {
            tasks = new ArrayList<>();
            logger.error("Unexpected Error", e);
        }

        return tasks;
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") long id) {
        Optional<Order> order;

        try {
            order = orderService.findById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if (order.isPresent()) {
            return new ResponseEntity<Order>(order.get(), HttpStatus.OK);
        }

        return new ResponseEntity<Order>(HttpStatus.GONE);
    }

    @PutMapping(value="/orders/{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody Order newOrder, @PathVariable Long id)
    {
        Optional<Order> oldOrder;
        newOrder.setId(id);

        try {
            oldOrder = orderService.findById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!oldOrder.isPresent()) {
            return new ResponseEntity<Order>(HttpStatus.GONE);
        }

        try {
            newOrder = orderService.update(newOrder, oldOrder.get());
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Order>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (newOrder == null) return new ResponseEntity<Order>(HttpStatus.CONFLICT);
        else return new ResponseEntity<Order>(newOrder, HttpStatus.OK);
    }

    @DeleteMapping(value="/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") long id){
        Optional<Order> order;

        try {
            order = orderService.findById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!order.isPresent()) {
            return new ResponseEntity<String>(HttpStatus.GONE);
        }

        try {
            orderService.delete(order.get());
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
}
