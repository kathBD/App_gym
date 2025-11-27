package com.sena.appspringboot.app.gym.service;

import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.repository.IRolRepository;
import com.sena.appspringboot.app.gym.repository.IUsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con los usuarios.
 * Esta clase se comunica con los repositorios para acceder a la base de datos.
 */
@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Método para cifrar todas las contraseñas que están en texto plano.
     * Ideal para usar una sola vez para migrar las contraseñas existentes.
     */
    @PostConstruct
    public void cifrarTodasLasContrasenas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getPassword() != null && !u.getPassword().startsWith("$2a$")) { // Verifica si no está cifrada
                String passCifrada = passwordEncoder.encode(u.getPassword());
                u.setPassword(passCifrada);
                usuarioRepository.save(u);
            }
        }
    }

    /**
     * Obtiene la lista completa de usuarios registrados en el sistema.
     *
     * @return Lista de usuarios.
     */
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene todos los usuarios que tienen asignado un rol específico (por nombre).
     * El nombre del rol no es sensible a mayúsculas/minúsculas.
     *
     * @param nombreRol Nombre del rol.
     * @return Lista de usuarios filtrados por el rol especificado.
     */
    public List<Usuario> getUsuariosPorRol(String nombreRol) {
        return usuarioRepository.findByRolNombreIgnoreCase(nombreRol);
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Optional con el usuario si existe.
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     */
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Guarda o actualiza un usuario.
     * - Codifica la contraseña si es nueva.
     * - Asigna el rol correcto.
     * - Mantiene la contraseña existente si no se modifica.
     * - Asigna 'activo = true' si es nulo.
     *
     * @param usuario Usuario a guardar o actualizar.
     * @return Usuario guardado.
     */
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        // Verificar y asignar el rol si viene con ID
        if (usuario.getRol() != null && usuario.getRol().getRolId() != null) {
            Rol rol = rolRepository.findById(usuario.getRol().getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuario.setRol(rol);
        }

        // Verificar que no exista un usuario con el mismo correo
        if (usuarioRepository.findByCorreo(usuario.getCorreo()) != null && usuario.getUsuarioId() == null) {
            throw new RuntimeException("Ya existe un usuario con ese correo.");
        }

        // Codificar contraseña si viene en el formulario
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            String passwordCifrada = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(passwordCifrada);
        } else if (usuario.getUsuarioId() != null) {
            // Si estás editando y no mandaste contraseña, conservar la actual
            String passwordActual = usuarioRepository.findById(usuario.getUsuarioId())
                    .map(Usuario::getPassword).orElse(null);
            usuario.setPassword(passwordActual);
        }

        // Si el campo 'activo' es null, asignamos true por defecto
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo electrónico del usuario.
     * @return Usuario encontrado o null si no existe.
     */
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }


}


