package com.sena.appspringboot.app.gym.controller;


import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.repository.IRolRepository;
import com.sena.appspringboot.app.gym.service.RolService;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private RolService rolService;



    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Buscar usuarios por nombre de rol (CLIENTE, ENTRENADOR, ADMINISTRADOR)
    @GetMapping("/rol/{nombreRol}")
    public List<Usuario> buscarPorRol(@PathVariable String nombreRol) {
        return usuarioService.getUsuariosPorRol(nombreRol);
    }

    // Crear un nuevo usuario (con contraseña cifrada automáticamente)
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.getAllRoles());
        return "registro_usuario";  // Thymeleaf buscará usuarios/registro_usuario.html
    }

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll()); // para llenar combo de roles

        return "usuario_form"; // Asegúrate que este archivo Thymeleaf exista
    }



    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
        return "redirect:/usuarios";
    }
    @PostMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        usuarioService.eliminarUsuario(id);
        redirectAttrs.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        model.addAttribute("usuario", usuario);
        return "ver_usuario"; // Nombre de la plantilla Thymeleaf que mostrarás
    }


}