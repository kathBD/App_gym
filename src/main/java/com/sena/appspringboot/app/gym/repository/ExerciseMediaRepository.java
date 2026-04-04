package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.ExerciseMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseMediaRepository extends JpaRepository<ExerciseMedia, Long> {

    Optional<ExerciseMedia> findByExerciseId(String exerciseId);

    boolean existsByExerciseId(String exerciseId);
}
