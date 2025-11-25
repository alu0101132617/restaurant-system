package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.MainCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MainCourseRepository extends JpaRepository<MainCourse, MainCourse.MainCourseId> {
    @Query("SELECT m FROM MainCourse m WHERE m.itemFood IS NOT NULL")
    List<MainCourse> findAll();
}

