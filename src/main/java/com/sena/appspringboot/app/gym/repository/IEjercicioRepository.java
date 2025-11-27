package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEjercicioRepository extends JpaRepository<Ejercicio, Long> {
}
