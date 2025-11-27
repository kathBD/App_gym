package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.TipoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoEjercicioRepository extends JpaRepository<TipoEjercicio, Long> {
}
