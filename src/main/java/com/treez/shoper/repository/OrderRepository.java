package com.treez.shoper.repository;

import org.springframework.data.repository.CrudRepository;

import com.treez.shoper.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
