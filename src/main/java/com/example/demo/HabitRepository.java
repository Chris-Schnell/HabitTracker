package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByOwnerOrderByDisplayOrderAsc(User owner);
    Optional<Habit> findByOwnerAndName(User owner, String name);
    Optional<Habit> findByIdAndOwner(Long id, User owner);
}
