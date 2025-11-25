package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.Appetizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppetizerRepository extends JpaRepository<Appetizer, Appetizer.AppetizerId> {
    @Query("SELECT a FROM Appetizer a WHERE a.itemAppetizers IS NOT NULL")
    List<Appetizer> findAll();
}

