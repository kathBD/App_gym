package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.RolService;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    // 1. MÉTODO PARA LISTAR (Faltaba en tu código y causaba el Ambiguous Mapping)
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.getAllUsuarios());
        return "usuarios"; // Nombre de tu tabla principal
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        List<Rol> roles = rolService.getAllRoles();
        Usuario usuario = new Usuario();
        usuario.setRol(new Rol());

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);
        return "registro_usuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // Validación de errores de formulario
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario";
        }

        // Verificar duplicados (solo si es nuevo usuario)
        if (usuario.getUsuarioId() == null && usuarioService.findByCorreo(usuario.getCorreo()) != null) {
            model.addAttribute("error", "Este correo electrónico ya está registrado.");
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario";
        }

        try {

            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "¡Usuario guardado con éxito.!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Error en la base de datos: " + e.getMessage());
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario";
        }
    }

    @GetMapping("/ver/{id}")
    public String mostrarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("usuario", usuario);
        return "ver_usuario";
    }

    // Corregido: hasRole es más robusto que hasAuthority para roles estándar
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.getAllRoles());
        return "registro_usuario"; // Reutilizamos la misma plantilla de registro
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el usuario seleccionado.");
        }
        return "redirect:/usuarios";
    }
}