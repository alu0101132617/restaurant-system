package es.ull.esit.app.middleware.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CashierController {

    private final CashierRepository cashierRepo;

    @Autowired
    public CashierController(CashierRepository cashierRepo) {
        this.cashierRepo = cashierRepo;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String,String>> info() {
        Map<String,String> info = new HashMap<>();
        info.put("name", "Mi Restaurante");
        info.put("address", "Calle Falsa 123");
        return ResponseEntity.ok(info);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> body) {
        String name = body.getOrDefault("name", "");
        Map<String,String> res = new HashMap<>();
        if (name == null || name.isBlank()) {
            res.put("status","error");
            res.put("message","missing name");
            return ResponseEntity.badRequest().body(res);
        }
        boolean exists = cashierRepo.findByName(name).isPresent();
        if (exists) {
            res.put("status","ok");
            res.put("welcome","Welcome " + name);
        } else {
            res.put("status","error");
            res.put("message","unknown cashier");
        }
        return ResponseEntity.ok(res);
    }
}
