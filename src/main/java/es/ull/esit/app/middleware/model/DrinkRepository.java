package es.ull.esit.app.middleware.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
}

