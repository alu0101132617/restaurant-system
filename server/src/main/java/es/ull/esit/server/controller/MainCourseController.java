package es.ull.esit.server.controller;

import es.ull.esit.server.middleware.model.MainCourse;
import es.ull.esit.server.repo.MainCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maincourse")
public class MainCourseController {

    @Autowired
    private MainCourseRepository mainCourseRepository;

    // GET todos los platos principales
    @GetMapping
    public ResponseEntity<List<MainCourse>> getAllMainCourses() {
        List<MainCourse> courses = mainCourseRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    // GET un plato principal por ID
    @GetMapping("/{id}")
    public ResponseEntity<MainCourse> getMainCourseById(@PathVariable Long id) {
        MainCourse.MainCourseId mainCourseId = new MainCourse.MainCourseId(id);
        Optional<MainCourse> course = mainCourseRepository.findById(mainCourseId);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST crear nuevo plato principal
    @PostMapping
    public ResponseEntity<MainCourse> createMainCourse(@RequestBody MainCourse mainCourse) {
        MainCourse saved = mainCourseRepository.save(mainCourse);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT actualizar plato principal
    @PutMapping("/{id}")
    public ResponseEntity<MainCourse> updateMainCourse(@PathVariable Long id, @RequestBody MainCourse mainCourse) {
        MainCourse.MainCourseId mainCourseId = new MainCourse.MainCourseId(id);
        if (!mainCourseRepository.existsById(mainCourseId)) {
            return ResponseEntity.notFound().build();
        }
        mainCourse.setFoodId(id);
        MainCourse updated = mainCourseRepository.save(mainCourse);
        return ResponseEntity.ok(updated);
    }

    // DELETE eliminar plato principal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMainCourse(@PathVariable Long id) {
        MainCourse.MainCourseId mainCourseId = new MainCourse.MainCourseId(id);
        if (!mainCourseRepository.existsById(mainCourseId)) {
            return ResponseEntity.notFound().build();
        }
        mainCourseRepository.deleteById(mainCourseId);
        return ResponseEntity.noContent().build();
    }
}
