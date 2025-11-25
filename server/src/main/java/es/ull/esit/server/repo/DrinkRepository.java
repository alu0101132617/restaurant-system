package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrinkRepository extends JpaRepository<Drink, Drink.DrinkId> {
    @Query("SELECT d FROM Drink d WHERE d.itemDrinks IS NOT NULL")
    List<Drink> findAll();
}

