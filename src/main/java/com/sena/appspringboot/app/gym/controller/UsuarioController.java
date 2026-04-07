package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;  // ← IMPORTANTE: Agregar esta importación
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;  // ← Agregar para cambiar contraseña

    // ========== CRUD USUARIOS ==========

    @GetMapping
    public List<Usuario> obtenerUsuariosApi() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/rol/{nombreRol}")
    public List<Usuario> buscarPorRolApi(@PathVariable String nombreRol) {
        return usuarioService.getUsuariosPorRol(nombreRol);
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuarioApi(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUnoApi(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarApi(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // ========== PERFIL ==========

    @GetMapping("/perfil")
    public ResponseEntity<Usuario> getPerfil(Principal principal) {
        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        // Ocultar la contraseña
        usuario.setPassword(null);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/perfil")
    public ResponseEntity<Usuario> updatePerfil(@RequestBody Usuario usuarioActualizado, Principal principal) {
        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualizar solo campos permitidos
        if (usuarioActualizado.getNombre() != null) {
            usuario.setNombre(usuarioActualizado.getNombre());
        }
        if (usuarioActualizado.getTelefono() != null) {
            usuario.setTelefono(usuarioActualizado.getTelefono());
        }
        if (usuarioActualizado.getSexo() != null) {
            usuario.setSexo(usuarioActualizado.getSexo());
        }
        if (usuarioActualizado.getPeso() != null) {
            usuario.setPeso(usuarioActualizado.getPeso());
        }
        if (usuarioActualizado.getEstatura() != null) {
            usuario.setEstatura(usuarioActualizado.getEstatura());
        }
        if (usuarioActualizado.getObjetivo() != null) {
            usuario.setObjetivo(usuarioActualizado.getObjetivo());
        }
        if (usuarioActualizado.getEstadoFisico() != null) {
            usuario.setEstadoFisico(usuarioActualizado.getEstadoFisico());
        }

        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody Map<String, String> passwords, Principal principal) {
        Usuario usuario = usuarioService.findByCorreo(principal.getName());
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwords.get("passwordActual"), usuario.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Contraseña actual incorrecta"));
        }

        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(passwords.get("passwordNueva")));
        usuarioService.guardarUsuario(usuario);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }
}
