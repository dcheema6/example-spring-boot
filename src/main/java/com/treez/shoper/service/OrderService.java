package com.treez.shoper.service;

import java.util.List;
import java.util.Optional;

import com.treez.shoper.model.Order;

public interface OrderService {    

    public Order create(Order order) throws Exception;
    public List<Order> getAsList() throws Exception;
    public Optional<Order> findById(long id) throws Exception;
    public Order update(Order newOrder, Order oldOrder) throws Exception;
    public void delete(Order order) throws Exception;
}
