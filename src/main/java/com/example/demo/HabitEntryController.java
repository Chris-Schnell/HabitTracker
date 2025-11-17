package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/entries")
@CrossOrigin // optional if you use a separate frontend
public class HabitEntryController {

    private final HabitEntryRepository repo;

    private final UserRepository userRepo;

    public HabitEntryController(HabitEntryRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<HabitEntry> getAll() {
        return repo.findAll();
    }





}
