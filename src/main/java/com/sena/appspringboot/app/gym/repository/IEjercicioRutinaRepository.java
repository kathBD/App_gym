package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.EjercicioRutina;
import com.sena.appspringboot.app.gym.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEjercicioRutinaRepository extends JpaRepository<EjercicioRutina, Long> {

    /**
     * Busca todos los ejercicios de una rutina específica
     * @param rutina La rutina
     * @return Lista de ejercicios de la rutina
     */
    List<EjercicioRutina> findByRutina(Rutina rutina);

    /**
     * Busca todos los ejercicios de una rutina por su ID
     * @param rutinaId ID de la rutina
     * @return Lista de ejercicios de la rutina
     */
    List<EjercicioRutina> findByRutina_RutinaId(Long rutinaId);

    /**
     * Elimina todos los ejercicios de una rutina
     * @param rutina La rutina
     */
    void deleteByRutina(Rutina rutina);

    /**
     * Busca ejercicios por ID de ejercicio del catálogo
     * @param ejercicioId ID del ejercicio
     * @return Lista de ejercicios en rutinas que usan ese ejercicio
     */
    List<EjercicioRutina> findByEjercicio_Id(String ejercicioId);
}