package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Appetizer;
import es.ull.esit.server.middleware.model.Drink;
import es.ull.esit.server.middleware.model.MainCourse;
import es.ull.esit.server.repo.AppetizerRepository;
import es.ull.esit.server.repo.DrinkRepository;
import es.ull.esit.server.repo.MainCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MainCourseRepository mainCourseRepository;

    @Autowired
    private AppetizerRepository appetizerRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    // GET menú completo
    @GetMapping
    public ResponseEntity<Map<String, Object>> getFullMenu() {
        Map<String, Object> menu = new HashMap<>();
        
        List<MainCourse> mainCourses = mainCourseRepository.findAll();
        List<Appetizer> appetizers = appetizerRepository.findAll();
        List<Drink> drinks = drinkRepository.findAll();
        
        menu.put("mainCourses", mainCourses);
        menu.put("appetizers", appetizers);
        menu.put("drinks", drinks);
        menu.put("totalItems", mainCourses.size() + appetizers.size() + drinks.size());
        
        return ResponseEntity.ok(menu);
    }
}
