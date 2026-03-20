package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "`force`")  // ← CORREGIDO: escapando la palabra reservada
    private String force;

    private String level;
    private String mechanic;
    private String equipment;
    private String category;

    @ElementCollection
    @CollectionTable(name = "exercise_primary_muscles",
            joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "muscle")
    private List<String> primaryMuscles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "exercise_secondary_muscles",
            joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "muscle")
    private List<String> secondaryMuscles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "exercise_instructions",
            joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "instruction", length = 1000)
    private List<String> instructions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "exercise_images",
            joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "image_url", length = 500)
    private List<String> images = new ArrayList<>();

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getForce() { return force; }
    public void setForce(String force) { this.force = force; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getMechanic() { return mechanic; }
    public void setMechanic(String mechanic) { this.mechanic = mechanic; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getPrimaryMuscles() { return primaryMuscles; }
    public void setPrimaryMuscles(List<String> primaryMuscles) { this.primaryMuscles = primaryMuscles; }

    public List<String> getSecondaryMuscles() { return secondaryMuscles; }
    public void setSecondaryMuscles(List<String> secondaryMuscles) { this.secondaryMuscles = secondaryMuscles; }

    public List<String> getInstructions() { return instructions; }
    public void setInstructions(List<String> instructions) { this.instructions = instructions; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}