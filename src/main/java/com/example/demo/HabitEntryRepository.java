package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitEntryRepository extends JpaRepository<HabitEntry, Long> {

    // per-user entry lookup
    Optional<HabitEntry> findByOwnerAndHabitNameAndDate(User owner, String habitName, LocalDate date);

    // per-user list of entries
    List<HabitEntry> findAllByOwnerAndHabitName(User owner, String habitName);
}
