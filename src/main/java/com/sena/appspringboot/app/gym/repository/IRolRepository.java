package com.sena.appspringboot.app.gym.repository;

import com.sena.appspringboot.app.gym.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface IRolRepository extends JpaRepository<Rol, Long> {



    // Método para buscar un rol por su nombre, devolviendo un Optional
    Optional<Rol> findByNombre(String nombre);

    // Método para encontrar todos los roles cuyo nombre comience con un prefijo específico
    List<Rol> findByNombreStartingWith(String prefix);

    // Método para contar el número de roles con un nombre específico
    long countByNombre(String nombre);

    // Método para encontrar todos los roles
    List<Rol> findAll();

}
