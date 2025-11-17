package com.example.demo;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/habits")
@CrossOrigin // adjust origins for production
public class HabitController {

    private final HabitRepository habitRepo;
    private final HabitEntryRepository entryRepo;
    private final UserRepository userRepo;

    public HabitController(HabitRepository habitRepo, HabitEntryRepository entryRepo, UserRepository userRepo) {
        this.habitRepo = habitRepo;
        this.entryRepo = entryRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<Habit> getAllHabits(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser();
        return habitRepo.findAllByOwnerOrderByDisplayOrderAsc(owner);
    }

    @PostMapping("/add")
    public Habit addHabit(@RequestParam String name, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser();
        int max = habitRepo.findAllByOwnerOrderByDisplayOrderAsc(owner).stream()
                .mapToInt(Habit::getDisplayOrder).max().orElse(0);
        Habit h = new Habit(name, max + 1, owner);
        return habitRepo.save(h);
    }

    @PostMapping("/entry/{habitName}/{date}/toggle")
    public HabitEntry toggleEntry(@PathVariable String habitName,
                                  @PathVariable String date,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser();

        Habit habit = habitRepo.findByOwnerAndName(owner, habitName)
                .orElseGet(() -> habitRepo.save(new Habit(habitName, 999, owner)));

        LocalDate localDate = LocalDate.parse(date);

        HabitEntry entry = entryRepo.findByOwnerAndHabitNameAndDate(owner, habit.getName(), localDate)
                .orElse(new HabitEntry(localDate, false, habit, owner));

        entry.setCompleted(!entry.isCompleted());
        return entryRepo.save(entry);
    }

    @GetMapping("/entry/{habitName}")
    public List<HabitEntry> getEntries(@PathVariable String habitName,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser();
        Habit habit = habitRepo.findByOwnerAndName(owner, habitName)
                .orElseThrow(() -> new RuntimeException("habit not found"));
        return entryRepo.findAllByOwnerAndHabitName(owner, habit.getName());
    }

    @DeleteMapping("/delete/{habitName}")
    public void deleteHabit(@PathVariable String habitName,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User owner = userDetails.getUser();
        habitRepo.findByOwnerAndName(owner, habitName).ifPresent(habit -> {
            entryRepo.findAllByOwnerAndHabitName(owner, habit.getName()).forEach(entryRepo::delete);
            habitRepo.delete(habit);
        });
    }
}
