package com.sena.appspringboot.app.gym.service;

import com.sena.appspringboot.app.gym.model.ExerciseMedia;
import com.sena.appspringboot.app.gym.repository.ExerciseMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WgerService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ExerciseMediaRepository mediaRepository;

    public List<Map<String, Object>> getExercises() {

        List<Map<String, Object>> ejercicios = new ArrayList<>();

        int offset = 0;
        int limit = 100;

        try {
            while (true) {

                String url = "https://wger.de/api/v2/exerciseinfo/?language=2&limit="
                        + limit + "&offset=" + offset;

                Map<String, Object> response =
                        restTemplate.getForObject(url, Map.class);

                if (response == null || !response.containsKey("results")) break;

                List<Map<String, Object>> results =
                        (List<Map<String, Object>>) response.get("results");

                if (results == null || results.isEmpty()) break;

                for (Map<String, Object> item : results) {

                    Map<String, Object> ejercicio = new HashMap<>();

                    // 🆔 ID
                    String id = item.get("id").toString();
                    ejercicio.put("id", id);

                    // 🧠 TRADUCCIONES
                    String name = "Sin nombre";
                    String description = "";

                    List<Map<String, Object>> translations =
                            (List<Map<String, Object>>) item.get("translations");

                    if (translations != null) {
                        for (Map<String, Object> t : translations) {
                            if (Integer.valueOf(2).equals(t.get("language"))) {
                                name = (String) t.getOrDefault("name", "Sin nombre");
                                description = (String) t.getOrDefault("description", "");
                                break;
                            }
                        }
                    }

                    ejercicio.put("name", name);
                    ejercicio.put("description", description);

                    // 🖼 IMAGEN WGER
                    String imageUrl = null;

                    List<Map<String, Object>> images =
                            (List<Map<String, Object>>) item.get("images");

                    if (images != null && !images.isEmpty()) {
                        Object imgObj = images.get(0).get("image");

                        if (imgObj != null) {
                            String img = imgObj.toString();
                            imageUrl = img.startsWith("/")
                                    ? "https://wger.de" + img
                                    : img;
                        }
                    }

                    ejercicio.put("imageUrl", imageUrl);

                    // 💪 CATEGORÍA
                    Map<String, Object> category =
                            (Map<String, Object>) item.get("category");

                    ejercicio.put("category",
                            category != null ? category.get("name") : "General");

                    // 🧬 MÚSCULOS
                    List<String> primaryMuscles = new ArrayList<>();

                    List<Map<String, Object>> muscles =
                            (List<Map<String, Object>>) item.get("muscles");

                    if (muscles != null) {
                        for (Map<String, Object> m : muscles) {
                            String muscle = (String) m.get("name_en");
                            if (muscle != null) {
                                primaryMuscles.add(muscle);
                            }
                        }
                    }

                    ejercicio.put("primaryMuscles", primaryMuscles);

                    // 🔥 GIF DESDE TU BD (exercise_media)
                    Optional<ExerciseMedia> media = mediaRepository.findByExerciseId(id);

                    if (media.isPresent()) {
                        ejercicio.put("gifUrl", media.get().getGifUrl());
                    } else {
                        ejercicio.put("gifUrl", null);
                    }

                    ejercicios.add(ejercicio);
                }

                offset += limit;
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR CONSUMIENDO WGER: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("✅ TOTAL EJERCICIOS WGER: " + ejercicios.size());

        return ejercicios;
    }
}