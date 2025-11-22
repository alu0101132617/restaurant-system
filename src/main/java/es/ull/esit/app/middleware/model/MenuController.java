package es.ull.esit.app.middleware.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MenuController {

    private final MainCourseRepository mainRepo;
    private final AppetizerRepository appetizerRepo;
    private final DrinkRepository drinkRepo;

    @Autowired
    public MenuController(MainCourseRepository mainRepo, AppetizerRepository appetizerRepo, DrinkRepository drinkRepo) {
        this.mainRepo = mainRepo;
        this.appetizerRepo = appetizerRepo;
        this.drinkRepo = drinkRepo;
    }

    @GetMapping("/menu")
    public ResponseEntity<Map<String,Object>> menu() {
        Map<String,Object> out = new HashMap<>();
        out.put("mains", mainRepo.findAll());
        out.put("appetizers", appetizerRepo.findAll());
        out.put("drinks", drinkRepo.findAll());
        return ResponseEntity.ok(out);
    }
}

