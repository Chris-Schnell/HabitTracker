package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Habit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int displayOrder;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Habit() {}
    public Habit(String name, int displayOrder, User owner) {
        this.name = name;
        this.displayOrder = displayOrder;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}
