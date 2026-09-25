package com.ecommece.dto;

import java.util.List;

public class CartDto {
    private Long id;
    private List<CartItemDto> items;
    private Double totalAmount;

    public CartDto() {}

    public CartDto(Long id, List<CartItemDto> items, Double totalAmount) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
