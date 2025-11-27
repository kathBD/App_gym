package com.sena.appspringboot.app.gym.repository;


import com.sena.appspringboot.app.gym.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Hereda de JpaRepository para aprovechar métodos CRUD ya definidos.
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuarios filtrados por el nombre del rol asociado.
     * Se realiza una comparación en minúsculas (case-insensitive).
     *
     * @param nombreRol El nombre del rol a filtrar (por ejemplo: "administrador").
     * @return Lista de usuarios cuyo rol coincida con el nombre proporcionado.
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.rol.nombre) = LOWER(:nombreRol)")
    List<Usuario> findByRolNombreIgnoreCase(@Param("nombreRol") String nombreRol);

    /**
     * Busca un usuario por correo electrónico, ignorando mayúsculas/minúsculas.
     *
     * Spring Data JPA genera automáticamente la consulta:
     *   SELECT u FROM Usuario u WHERE LOWER(u.correo) = LOWER(?1)
     *
     * @param correo Correo electrónico del usuario.
     * @return Optional con el usuario encontrado (si existe).
     */


    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    Usuario findByCorreo(String correo);

    List<Usuario> findByObjetivoContainingIgnoreCase(String objetivo);

}
