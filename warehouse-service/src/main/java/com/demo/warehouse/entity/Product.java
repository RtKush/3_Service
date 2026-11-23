package com.demo.warehouse.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String sku;
    @Column(nullable=false)
    private String name;
    private int quantity;
    private int reorderLevel;
    private double price;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getSku(){return sku;} public void setSku(String sku){this.sku=sku;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public int getQuantity(){return quantity;} public void setQuantity(int quantity){this.quantity=quantity;}
    public int getReorderLevel(){return reorderLevel;} public void setReorderLevel(int reorderLevel){this.reorderLevel=reorderLevel;}
    public double getPrice(){return price;} public void setPrice(double price){this.price=price;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant v){updatedAt=v;}
}
