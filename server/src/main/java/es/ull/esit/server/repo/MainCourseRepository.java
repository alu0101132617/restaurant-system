package es.ull.esit.server.repo;

import es.ull.esit.server.middleware.model.MainCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCourseRepository extends JpaRepository<MainCourse, Long> {
}

