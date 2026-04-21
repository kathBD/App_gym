package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRutinaRepository extends JpaRepository<Rutina, Long> {

    List<Rutina> findByClienteId(Long clienteId);
    List<Rutina> findByCreadorId(Long creadorId);
    List<Rutina> findByNombreContainingIgnoreCase(String nombre);
    List<Rutina> findByActivoTrue();
    List<Rutina> findByNivel(String nivel);
}