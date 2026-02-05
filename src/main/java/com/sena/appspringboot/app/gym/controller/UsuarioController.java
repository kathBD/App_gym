package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. OBTENER TODOS LOS USUARIOS
    // GET http://localhost:8080/api/usuarios
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // 2. OBTENER UN USUARIO POR ID (Ver detalle)
    // GET http://localhost:8080/api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 3. BUSCAR POR ROL
    // GET http://localhost:8080/api/usuarios/rol/ENTRENADOR
    @GetMapping("/rol/{nombreRol}")
    public List<Usuario> buscarPorRol(@PathVariable String nombreRol) {
        return usuarioService.getUsuariosPorRol(nombreRol);
    }

    // 4. CREAR USUARIO
    // POST http://localhost:8080/api/usuarios
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    // 5. EDITAR USUARIO (Actualización total/parcial)
    // PUT http://localhost:8080/api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioEditado) {
        return usuarioService.obtenerPorId(id)
                .map(usuarioExistente -> {
                    usuarioEditado.setUsuarioId(id); // Aseguramos que edite el ID correcto
                    Usuario guardado = usuarioService.guardarUsuario(usuarioEditado);
                    return new ResponseEntity<>(guardado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 6. ELIMINAR USUARIO
    // DELETE http://localhost:8080/api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content es lo ideal para delete
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}