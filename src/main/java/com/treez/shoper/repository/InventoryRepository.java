package com.treez.shoper.repository;

import org.springframework.data.repository.CrudRepository;

import com.treez.shoper.model.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

}
