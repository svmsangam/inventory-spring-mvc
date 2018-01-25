package com.inventory.core.model.entity;

import com.inventory.core.model.enumconstant.Status;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dhiraj on 1/25/18.
 */
@Entity
@Table(name = "sevice_table")
public class ServiceInfo extends AbstractEntity<Long>{

    private int totalStore;

    private int expireDays;

    private double rate;

    private Status status;

    public int getTotalStore() {
        return totalStore;
    }

    public void setTotalStore(int totalStore) {
        this.totalStore = totalStore;
    }

    public int getExpireDays() {
        return expireDays;
    }

    public void setExpireDays(int expireDays) {
        this.expireDays = expireDays;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
