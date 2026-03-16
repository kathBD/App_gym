package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.security.JwtUtil;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String correo   = request.get("correo");
        String password = request.get("password");

        Usuario usuario = usuarioService.findByCorreo(correo);
        if (usuario == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));

        if (usuario.getActivo() == null || !usuario.getActivo())
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Usuario inactivo"));

        String nombreRol = usuario.getRol().getNombre().toUpperCase();

        String token = jwtUtil.generateToken(
                usuario.getUsuarioId(), usuario.getCorreo(), nombreRol);

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("usuarioId", usuario.getUsuarioId());
        usuarioData.put("correo",    usuario.getCorreo());
        usuarioData.put("nombre",    usuario.getNombre());
        usuarioData.put("telefono",  usuario.getTelefono());
        usuarioData.put("rol",       Map.of("nombre", nombreRol));
        usuarioData.put("activo",    usuario.getActivo());

        Map<String, Object> response = new HashMap<>();
        response.put("token",   token);
        response.put("usuario", usuarioData);

        return ResponseEntity.ok(response);
    }
}
