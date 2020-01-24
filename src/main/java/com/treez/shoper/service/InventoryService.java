package com.treez.shoper.service;

import java.util.List;
import java.util.Optional;

import com.treez.shoper.model.Inventory;

public interface InventoryService {    

    public Inventory create(Inventory inventory) throws Exception;
    public List<Inventory> getAsList() throws Exception;
    public Optional<Inventory> findById(long id) throws Exception;
    public Inventory update(Inventory inventory) throws Exception;
    public void deleteById(long id) throws Exception;
}
