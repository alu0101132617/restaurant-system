package es.ull.esit.app.middleware.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    Optional<Cashier> findByName(String name);
}

