package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IRutinaRepository extends JpaRepository<Rutina, Integer> {
    List<Rutina> findByUsuarioUsuarioId(Long usuarioId);
}
