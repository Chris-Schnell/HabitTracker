package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class HabitEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public HabitEntry() {}

    public HabitEntry(LocalDate date, boolean completed, Habit habit, User owner) {
        this.date = date;
        this.completed = completed;
        this.habit = habit;
        this.owner = owner;
    }



    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Habit getHabit() { return habit; }
    public void setHabit(Habit habit) { this.habit = habit; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}
