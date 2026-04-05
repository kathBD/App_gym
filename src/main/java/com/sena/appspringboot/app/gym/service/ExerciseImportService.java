package com.sena.appspringboot.app.gym.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sena.appspringboot.app.gym.model.Exercise;
import com.sena.appspringboot.app.gym.repository.ExerciseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExerciseImportService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    //@PostConstruct
    @Transactional
    public void importExercises() {
        try {
            System.out.println("📥 INICIANDO IMPORTACIÓN DE EJERCICIOS...");

            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("exercises.json").getInputStream();
            List<Map<String, Object>> data = mapper.readValue(
                    inputStream, new TypeReference<List<Map<String, Object>>>() {});

            System.out.println("📄 Archivo JSON cargado con " + data.size() + " ejercicios");

            int count = 0;
            for (Map<String, Object> item : data) {
                try {
                    Exercise exercise = new Exercise();
                    exercise.setId((String) item.get("id"));
                    exercise.setName((String) item.get("name"));
                    exercise.setForce((String) item.get("force"));
                    exercise.setLevel((String) item.get("level"));
                    exercise.setMechanic((String) item.get("mechanic"));
                    exercise.setEquipment((String) item.get("equipment"));
                    exercise.setCategory((String) item.get("category"));
                    exercise.setPrimaryMuscles(castList(item.get("primaryMuscles")));
                    exercise.setSecondaryMuscles(castList(item.get("secondaryMuscles")));
                    exercise.setInstructions(castList(item.get("instructions")));
                    exercise.setImages(castList(item.get("images")));

                    exerciseRepository.save(exercise);
                    count++;

                    if (count % 100 == 0) {
                        System.out.println("⏳ Progreso: " + count + " ejercicios guardados...");
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error guardando ejercicio: " + e.getMessage());
                }
            }

            System.out.println("✅ IMPORTACIÓN COMPLETADA: " + count + " ejercicios");

        } catch (Exception e) {
            System.err.println("❌ ERROR EN IMPORTACIÓN: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<String> castList(Object obj) {
        if (obj instanceof List) {
            return (List<String>) obj;
        }
        return new ArrayList<>();
    }
}