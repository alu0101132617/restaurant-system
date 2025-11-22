package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Appetizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppetizerRepository extends JpaRepository<Appetizer, Long> {
}

