package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.service.WgerService;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wger")
@CrossOrigin(origins = "*")
public class WgerController {

    private final WgerService wgerService;

    public WgerController(WgerService wgerService) {
        this.wgerService = wgerService;
    }

    @GetMapping("/exercises")
    public List<Map<String, Object>> getExercises() {
        return wgerService.getExercises();
    }
}