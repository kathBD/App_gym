package com.sena.appspringboot.app.gym.service;

import com.sena.appspringboot.app.gym.model.Exercise;
import com.sena.appspringboot.app.gym.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    public List<Exercise> getAllExercises() {
        System.out.println("ExerciseService.getAllExercises() llamado");
        List<Exercise> exercises = exerciseRepository.findAll();
        System.out.println("Ejercicios encontrados: " + exercises.size());
        return exercises;
    }

    public Optional<Exercise> getExerciseById(String id) {
        return exerciseRepository.findById(id);
    }

    public List<Exercise> getByEquipment(String equipment) {
        return exerciseRepository.findByEquipmentIgnoreCase(equipment);
    }

    public List<Exercise> getByCategory(String category) {
        return exerciseRepository.findByCategoryIgnoreCase(category);
    }

    public List<Exercise> getByLevel(String level) {
        return exerciseRepository.findByLevelIgnoreCase(level);
    }

    public List<Exercise> searchExercises(String keyword) {
        return exerciseRepository.searchByName(keyword);
    }

    public List<Exercise> getByMuscle(String muscle) {
        return exerciseRepository.findByPrimaryMuscle(muscle);
    }

    public List<String> getAllEquipments() {
        return exerciseRepository.findAllEquipments();
    }

    public List<String> getAllCategories() {
        return exerciseRepository.findAllCategories();
    }

    public List<String> getAllMuscles() {
        return exerciseRepository.findAllPrimaryMuscles();
    }
}
