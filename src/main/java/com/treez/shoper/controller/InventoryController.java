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

import com.treez.shoper.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.treez.shoper.model.Inventory;

@RestController
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @PostMapping(path = "/inventories")
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {

        try {
            inventory = inventoryService.create(inventory);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Inventory>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (inventory == null) {
            return new ResponseEntity<Inventory>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<Inventory>(inventory, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/inventories")
    public List<Inventory> getInventoryList() {
        List<Inventory> tasks;

        try {
            tasks = inventoryService.getAsList();
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            tasks = new ArrayList<>();
        }

        return tasks;
    }

    @GetMapping(value = "/inventories/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable("id") long id) {
        Optional<Inventory> inventory;

        try {
            inventory = inventoryService.findById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Inventory>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if (!inventory.isPresent()) {
            return new ResponseEntity<Inventory>(HttpStatus.GONE);
        }

        return new ResponseEntity<Inventory>(inventory.get(), HttpStatus.OK);
    }

    @PutMapping(value="/inventories/{id}")
    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory, @PathVariable Long id)
    {
        Optional<Inventory> optInventory;
        inventory.setId(id);
        
        try {
            optInventory = inventoryService.findById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Inventory>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!optInventory.isPresent()) {
            return new ResponseEntity<Inventory>(HttpStatus.GONE);
        }

        try {
            inventory = inventoryService.update(inventory);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<Inventory>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (inventory == null) return new ResponseEntity<Inventory>(HttpStatus.CONFLICT);
        else return new ResponseEntity<Inventory>(inventory, HttpStatus.OK);
    }

    @DeleteMapping(value="/inventories/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable("id") long id){
        Optional<Inventory> inventory;
        
        try {
            inventory = inventoryService.findById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!inventory.isPresent()) {
            return new ResponseEntity<String>(HttpStatus.GONE);
        }

        try {
            inventoryService.deleteById(id);
        } catch (Exception e) {
            logger.error("Unexpected Error", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
}
