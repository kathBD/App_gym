package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios") // Ruta para datos: localhost:8080/api/usuarios
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios en JSON
    @GetMapping
    public List<Usuario> obtenerUsuariosApi() {
        return usuarioService.getAllUsuarios();
    }

    // Buscar por rol en JSON
    @GetMapping("/rol/{nombreRol}")
    public List<Usuario> buscarPorRolApi(@PathVariable String nombreRol) {
        return usuarioService.getUsuariosPorRol(nombreRol);
    }

    // Crear usuario desde una petición REST (Postman o Angular)
    @PostMapping
    public ResponseEntity<Usuario> crearUsuarioApi(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    // Obtener uno solo por ID en JSON
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUnoApi(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar vía API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarApi(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
