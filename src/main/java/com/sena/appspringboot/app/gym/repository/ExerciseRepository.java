package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, String> {

    List<Exercise> findByEquipmentIgnoreCase(String equipment);

    List<Exercise> findByCategoryIgnoreCase(String category);

    List<Exercise> findByLevelIgnoreCase(String level);

    @Query("SELECT e FROM Exercise e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Exercise> searchByName(@Param("keyword") String keyword);

    @Query("SELECT e FROM Exercise e JOIN e.primaryMuscles m WHERE LOWER(m) LIKE LOWER(CONCAT('%', :muscle, '%'))")
    List<Exercise> findByPrimaryMuscle(@Param("muscle") String muscle);

    @Query("SELECT DISTINCT e.equipment FROM Exercise e WHERE e.equipment IS NOT NULL")
    List<String> findAllEquipments();

    @Query("SELECT DISTINCT e.category FROM Exercise e WHERE e.category IS NOT NULL")
    List<String> findAllCategories();

    @Query("SELECT DISTINCT m FROM Exercise e JOIN e.primaryMuscles m")
    List<String> findAllPrimaryMuscles();
}
