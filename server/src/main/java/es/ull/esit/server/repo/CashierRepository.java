package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    Optional<Cashier> findByName(String name);
}

