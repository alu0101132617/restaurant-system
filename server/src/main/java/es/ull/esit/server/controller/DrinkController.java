package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.Drink;
import es.ull.esit.server.repo.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

    @Autowired
    private DrinkRepository drinkRepository;

    // GET todas las bebidas
    @GetMapping
    public ResponseEntity<List<Drink>> getAllDrinks() {
        List<Drink> drinks = drinkRepository.findAll();
        return ResponseEntity.ok(drinks);
    }

    // GET una bebida por ID
    @GetMapping("/{id}")
    public ResponseEntity<Drink> getDrinkById(@PathVariable Long id) {
        Drink.DrinkId drinkId = new Drink.DrinkId(id);
        Optional<Drink> drink = drinkRepository.findById(drinkId);
        return drink.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST crear nueva bebida
    @PostMapping
    public ResponseEntity<Drink> createDrink(@RequestBody Drink drink) {
        Drink saved = drinkRepository.save(drink);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT actualizar bebida
    @PutMapping("/{id}")
    public ResponseEntity<Drink> updateDrink(@PathVariable Long id, @RequestBody Drink drink) {
        Drink.DrinkId drinkId = new Drink.DrinkId(id);
        if (!drinkRepository.existsById(drinkId)) {
            return ResponseEntity.notFound().build();
        }
        drink.setDrinksId(id);
        Drink updated = drinkRepository.save(drink);
        return ResponseEntity.ok(updated);
    }

    // DELETE eliminar bebida
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrink(@PathVariable Long id) {
        Drink.DrinkId drinkId = new Drink.DrinkId(id);
        if (!drinkRepository.existsById(drinkId)) {
            return ResponseEntity.notFound().build();
        }
        drinkRepository.deleteById(drinkId);
        return ResponseEntity.noContent().build();
    }
}
