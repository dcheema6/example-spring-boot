package com.treez.shoper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.treez.shoper.model.Inventory;
import com.treez.shoper.model.Order;
import com.treez.shoper.repository.OrderRepository;
import com.treez.shoper.repository.InventoryRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    public Order create(Order order) throws Exception {
        if (order == null) return null;
        order.setStatus(Order.Status.created.toString());

        Map<Long, Integer> quantities = order.getQuantities();
        if (quantities == null) return null;

        List<Inventory> invList = new ArrayList<>(quantities.size());
        logger.info("Quantities: " + quantities);

        for (long invId : quantities.keySet()) {
            // Check if requred inventory exists
            Optional<Inventory> optInv = inventoryRepository.findById(invId);
            if (!optInv.isPresent()) return null;

            // Check if quantity needed is valid
            Inventory inv = optInv.get();
            int qNeeded = quantities.get(invId);
            int qAvailable = inv.getQuantity();
            if (qNeeded <= 0 || qNeeded > qAvailable) return null;

            // Update quantity
            logger.info("INV Setting: " + inv);
            inv.setQuantity(qAvailable - qNeeded);
            invList.add(inv);
        }

        for (Inventory inv : invList) {
            logger.info("INV Saving: " + inv);
            inventoryRepository.save(inv);
        }

        return orderRepository.save(order);
    }

    public List<Order> getAsList() throws Exception {
        return (List<Order>) orderRepository.findAll();
    }

    public Optional<Order> findById(long id) throws Exception {
        return orderRepository.findById(id);
    }

    public Order update(Order newOrder, Order oldOrder) throws Exception {
        if (newOrder == null || oldOrder == null) return null;
        newOrder.setDateCreated(oldOrder.getDateCreated());
        newOrder.setDateUpdated(oldOrder.getDateUpdated());

        logger.info("Status new: " + newOrder.getStatus() + ", old: " + oldOrder.getStatus());

        // If status did not change
        if (oldOrder.getStatus().equals(newOrder.getStatus())) {
            // Inventory quantities might need to be updated
            if (Order.Status.created.toString().equals(newOrder.getStatus())) {
                // To update inventory quantities, cancel(oldOrder) and create(newOrder)
                // This way is not the most efficient but has least complexity
                logger.info("Canceling order");
                cancel(oldOrder);
                
                logger.info("Creating order");
                if (create(newOrder) == null) {
                    // Recreate oldOrder to undo cancel operation in case creation of new order fails
                    logger.info("Could not create newOrder, creating oldOrder");
                    create(oldOrder);
                    return null;
                }
            }
            // If status is canceled then Inventory need not be updated
            else return orderRepository.save(newOrder);
        }
        // Status changed from canceled to created
        else if (Order.Status.created.toString().equals(newOrder.getStatus())) return create(newOrder);
        // Status changed from created to cancel
        else if (Order.Status.canceled.toString().equals(newOrder.getStatus())) return cancel(newOrder);
        
        // Status is not valid
        return null;
    }

    public void delete(Order order) throws Exception {
        cancel(order);
        orderRepository.deleteById(order.getId());
    }

    private Order cancel(Order order) throws Exception {
        order.setStatus(Order.Status.canceled.toString());

        Map<Long, Integer> quantities = order.getQuantities();

        for (long invId : quantities.keySet()) {
            // Check if requred inventory exists
            Optional<Inventory> optInv = inventoryRepository.findById(invId);
            if (!optInv.isPresent()) continue;

            // Update quantity
            Inventory inv = optInv.get();
            inv.setQuantity(inv.getQuantity() + quantities.get(invId));
            inventoryRepository.save(inv);
        }

        return orderRepository.save(order);
    }
}
