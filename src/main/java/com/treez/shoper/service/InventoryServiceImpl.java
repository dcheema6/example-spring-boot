package com.treez.shoper.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.treez.shoper.model.Inventory;
import com.treez.shoper.repository.InventoryRepository;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    
    @Autowired
    InventoryRepository inventoryRepository;

    public Inventory create(Inventory inventory) throws Exception {
        if (inventory == null || inventory.getQuantity() < 0 || inventory.getPrice() < 0) return null;
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getAsList() throws Exception {
        return (List<Inventory>) inventoryRepository.findAll();
    }

    public Optional<Inventory> findById(long id) throws Exception {
        return inventoryRepository.findById(id);
    }

    public Inventory update(Inventory inventory) throws Exception {
        if (inventory == null || inventory.getQuantity() < 0 || inventory.getPrice() < 0) return null;
        return inventoryRepository.save(inventory);
    }

    public void deleteById(long id) throws Exception {
        inventoryRepository.deleteById(id);
    }
}
