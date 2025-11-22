package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
}

