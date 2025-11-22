package es.ull.esit.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CashierController {

    private static final List<Map<String,Object>> MENU = new ArrayList<>();
    static {
        Map<String,Object> m1 = new HashMap<>(); m1.put("id",1); m1.put("name","Pizza"); m1.put("price",8.5);
        Map<String,Object> m2 = new HashMap<>(); m2.put("id",2); m2.put("name","Ensalada"); m2.put("price",5.0);
        MENU.add(m1); MENU.add(m2);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String,String>> info() {
        Map<String,String> info = new HashMap<>();
        info.put("name", "Mi Restaurante");
        info.put("address", "Calle Falsa 123");
        return ResponseEntity.ok(info);
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Map<String,Object>>> menu() {
        return ResponseEntity.ok(MENU);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> body) {
        String name = body.getOrDefault("name", "Cashier");
        Map<String,String> res = new HashMap<>();
        res.put("status", "ok");
        res.put("welcome", "Welcome " + name);
        return ResponseEntity.ok(res);
    }
}

