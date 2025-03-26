package com.example.prm392.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderDate;
    private String totalPrice;
    private List<Cart> listProduct;
    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String orderDate, String totalPrice, List<Cart> listProduct) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.listProduct = listProduct;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Cart> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Cart> listProduct) {
        this.listProduct = listProduct;
    }
}
