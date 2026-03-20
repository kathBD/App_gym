package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Exercise;
import com.sena.appspringboot.app.gym.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "http://localhost:4200")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<?> getAllExercises() {
        try {
            System.out.println("📥 Petición recibida en /api/exercises");
            List<Exercise> exercises = exerciseService.getAllExercises();
            System.out.println("✅ Ejercicios obtenidos: " + exercises.size());
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            System.err.println("❌ ERROR EN EL CONTROLADOR: " + e.getMessage());
            e.printStackTrace();  // Esto imprime el error COMPLETO
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return exerciseService.getExerciseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Exercise>> search(@RequestParam String q) {
        return ResponseEntity.ok(exerciseService.searchExercises(q));
    }

    @GetMapping("/equipment/{equipment}")
    public ResponseEntity<List<Exercise>> byEquipment(@PathVariable String equipment) {
        return ResponseEntity.ok(exerciseService.getByEquipment(equipment));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Exercise>> byCategory(@PathVariable String category) {
        return ResponseEntity.ok(exerciseService.getByCategory(category));
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<Exercise>> byLevel(@PathVariable String level) {
        return ResponseEntity.ok(exerciseService.getByLevel(level));
    }

    @GetMapping("/muscle/{muscle}")
    public ResponseEntity<List<Exercise>> byMuscle(@PathVariable String muscle) {
        return ResponseEntity.ok(exerciseService.getByMuscle(muscle));
    }

    @GetMapping("/equipments")
    public ResponseEntity<List<String>> equipments() {
        return ResponseEntity.ok(exerciseService.getAllEquipments());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> categories() {
        return ResponseEntity.ok(exerciseService.getAllCategories());
    }

    @GetMapping("/muscles")
    public ResponseEntity<List<String>> muscles() {
        return ResponseEntity.ok(exerciseService.getAllMuscles());
    }
}