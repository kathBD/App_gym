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

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Cifra contraseñas en texto plano al iniciar la app (Migración).
     */
    @PostConstruct
    public void cifrarTodasLasContrasenas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            if (u.getPassword() != null && !u.getPassword().startsWith("$2a$")) {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                usuarioRepository.save(u);
            }
        }
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> getUsuariosPorRol(String nombreRol) {
        return usuarioRepository.findByRolNombreIgnoreCase(nombreRol);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Guarda un usuario nuevo o actualiza uno existente de forma selectiva.
     */
    @Transactional
    public Usuario guardarUsuario(Usuario usuarioForm) {
        Usuario usuarioParaGuardar;

        if (usuarioForm.getUsuarioId() != null) {
            // --- CASO: EDICIÓN ---
            // 1. Recuperamos el usuario actual de la BD
            usuarioParaGuardar = usuarioRepository.findById(usuarioForm.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioForm.getUsuarioId()));

            // 2. ACTUALIZACIÓN DE CAMPOS (Mapeo manual para no perder datos)
            usuarioParaGuardar.setNombre(usuarioForm.getNombre());
            usuarioParaGuardar.setCorreo(usuarioForm.getCorreo());
            usuarioParaGuardar.setTelefono(usuarioForm.getTelefono());
            usuarioParaGuardar.setSexo(usuarioForm.getSexo());
            usuarioParaGuardar.setFechaNacimiento(usuarioForm.getFechaNacimiento());

            // Campos físicos (¡Corregido!)
            usuarioParaGuardar.setPeso(usuarioForm.getPeso());
            usuarioParaGuardar.setEstatura(usuarioForm.getEstatura());

            // Campos de perfil (¡Corregido!)
            usuarioParaGuardar.setObjetivo(usuarioForm.getObjetivo());
            usuarioParaGuardar.setEstadoFisico(usuarioForm.getEstadoFisico());

            // Campos específicos de Entrenador (¡Corregido!)
            usuarioParaGuardar.setEspecialidad(usuarioForm.getEspecialidad());
            usuarioParaGuardar.setHorarioInicio(usuarioForm.getHorarioInicio());
            usuarioParaGuardar.setHorarioFin(usuarioForm.getHorarioFin());

            if (usuarioForm.getActivo() != null) {
                usuarioParaGuardar.setActivo(usuarioForm.getActivo());
            }

            // 3. Actualización de Rol
            if (usuarioForm.getRol() != null && usuarioForm.getRol().getRolId() != null) {
                Rol rol = rolRepository.findById(usuarioForm.getRol().getRolId())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                usuarioParaGuardar.setRol(rol);
            }

            // 4. Gestión de Contraseña
            if (usuarioForm.getPassword() != null && !usuarioForm.getPassword().isBlank()) {
                // Solo encriptamos si es una nueva contraseña (no es un hash)
                if (!usuarioForm.getPassword().startsWith("$2a$")) {
                    usuarioParaGuardar.setPassword(passwordEncoder.encode(usuarioForm.getPassword()));
                }
            }

        } else {
            // --- CASO: NUEVO REGISTRO ---
            usuarioParaGuardar = usuarioForm;

            // Validar correo único
            if (usuarioRepository.findByCorreo(usuarioParaGuardar.getCorreo()) != null) {
                throw new RuntimeException("El correo ya está registrado.");
            }

            // Cifrar contraseña
            if (usuarioParaGuardar.getPassword() != null) {
                usuarioParaGuardar.setPassword(passwordEncoder.encode(usuarioParaGuardar.getPassword()));
            }

            // Asignar rol
            if (usuarioParaGuardar.getRol() != null && usuarioParaGuardar.getRol().getRolId() != null) {
                Rol rol = rolRepository.findById(usuarioParaGuardar.getRol().getRolId())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                usuarioParaGuardar.setRol(rol);
            }

            if (usuarioParaGuardar.getActivo() == null) {
                usuarioParaGuardar.setActivo(true);
            }
        }

        // 5. Persistencia final
        return usuarioRepository.save(usuarioParaGuardar);
    }

    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}


