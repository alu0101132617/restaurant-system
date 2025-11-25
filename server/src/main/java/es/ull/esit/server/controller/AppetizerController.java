package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Appetizer;
import es.ull.esit.server.repo.AppetizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appetizers")
public class AppetizerController {

    @Autowired
    private AppetizerRepository appetizerRepository;

    // GET todos los aperitivos
    @GetMapping
    public ResponseEntity<List<Appetizer>> getAllAppetizers() {
        List<Appetizer> appetizers = appetizerRepository.findAll();
        return ResponseEntity.ok(appetizers);
    }

    // GET un aperitivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Appetizer> getAppetizerById(@PathVariable Long id) {
        Appetizer.AppetizerId appetizerId = new Appetizer.AppetizerId(id);
        Optional<Appetizer> appetizer = appetizerRepository.findById(appetizerId);
        return appetizer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST crear nuevo aperitivo
    @PostMapping
    public ResponseEntity<Appetizer> createAppetizer(@RequestBody Appetizer appetizer) {
        Appetizer saved = appetizerRepository.save(appetizer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT actualizar aperitivo
    @PutMapping("/{id}")
    public ResponseEntity<Appetizer> updateAppetizer(@PathVariable Long id, @RequestBody Appetizer appetizer) {
        Appetizer.AppetizerId appetizerId = new Appetizer.AppetizerId(id);
        if (!appetizerRepository.existsById(appetizerId)) {
            return ResponseEntity.notFound().build();
        }
        appetizer.setAppetizersId(id);
        Appetizer updated = appetizerRepository.save(appetizer);
        return ResponseEntity.ok(updated);
    }

    // DELETE eliminar aperitivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppetizer(@PathVariable Long id) {
        Appetizer.AppetizerId appetizerId = new Appetizer.AppetizerId(id);
        if (!appetizerRepository.existsById(appetizerId)) {
            return ResponseEntity.notFound().build();
        }
        appetizerRepository.deleteById(appetizerId);
        return ResponseEntity.noContent().build();
    }
}
