package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Cashier;
import es.ull.esit.server.repo.CashierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cashiers")
public class CashierController {

    @Autowired
    private CashierRepository cashierRepository;

    // GET todos los cajeros
    @GetMapping
    public ResponseEntity<List<Cashier>> getAllCashiers() {
        List<Cashier> cashiers = cashierRepository.findAll();
        return ResponseEntity.ok(cashiers);
    }

    // GET un cajero por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cashier> getCashierById(@PathVariable Long id) {
        Optional<Cashier> cashier = cashierRepository.findById(id);
        return cashier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET un cajero por nombre
    @GetMapping("/name/{name}")
    public ResponseEntity<Cashier> getCashierByName(@PathVariable String name) {
        Optional<Cashier> cashier = cashierRepository.findByName(name);
        return cashier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST crear nuevo cajero
    @PostMapping
    public ResponseEntity<Cashier> createCashier(@RequestBody Cashier cashier) {
        Cashier saved = cashierRepository.save(cashier);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT actualizar cajero
    @PutMapping("/{id}")
    public ResponseEntity<Cashier> updateCashier(@PathVariable Long id, @RequestBody Cashier cashier) {
        if (!cashierRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cashier.setId(id);
        Cashier updated = cashierRepository.save(cashier);
        return ResponseEntity.ok(updated);
    }

    // DELETE eliminar cajero
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashier(@PathVariable Long id) {
        if (!cashierRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cashierRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
