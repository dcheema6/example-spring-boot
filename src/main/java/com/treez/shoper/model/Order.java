package com.treez.shoper.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.treez.shoper.utils.KeyValueContainer;
import com.treez.shoper.utils.ObjectUtils;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String customerEmail;
    @CreationTimestamp
    private Timestamp dateCreated;
    @UpdateTimestamp
    private Timestamp dateUpdated;
    private String status;
    @ElementCollection
    @CollectionTable(name = "INV_QUANTITIES")
    @MapKeyColumn(name = "INV_ID")
    @Column(name = "QUANTITY")
    @JsonIgnore
    private Map<Long, Integer> quantities;

    public Order() {
        this.status = Status.created.toString();
        quantities = new HashMap<Long, Integer>();
    }

    public Order(long id, String customerEmail, Timestamp dateCreated, Timestamp dateUpdated,
        Map<Long, Integer> quantities) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.status = Status.created.toString();
        this.quantities = quantities;
    }

    public enum Status {
        created,
        canceled
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (Status.created.toString().equals(status) || Status.canceled.toString().equals(status)) {
            this.status = status;
        }
    }

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Long, Integer> quantities) {
        this.quantities = quantities;
    }

    @JsonProperty("quantities")
    private List<KeyValueContainer<Long, Integer>> getTeamList() {
        return ObjectUtils.toList(quantities);
    }

    @JsonProperty("quantities")
    private void setTeamList(List<KeyValueContainer<Long, Integer>> list) {
        quantities = ObjectUtils.toMap(list);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order {");
        sb.append("id=").append(id);
        sb.append(", name='").append(customerEmail).append('\'');
        sb.append(", dateCreated='").append(dateCreated).append('\'');
        sb.append(", dateUpdated='").append(dateUpdated).append('\'');
        sb.append(", status=").append(status).append('\'');
        sb.append(", quantities=").append(quantities);
        sb.append('}');
        return sb.toString();
    }
}
