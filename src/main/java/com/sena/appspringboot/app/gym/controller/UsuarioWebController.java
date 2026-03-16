package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.RolService;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public String listarUsuarios(@RequestParam(value = "rol", required = false) String rol, Model model) {
        List<Usuario> usuarios;
        if (rol != null && !rol.isEmpty()) {
            usuarios = usuarioService.getUsuariosPorRol(rol);
            model.addAttribute("rolFiltrado", rol);
        } else {
            usuarios = usuarioService.getAllUsuarios();
        }
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        Usuario usuario = new Usuario();
        usuario.setRol(new Rol());
        usuario.setActivo(true);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.getAllRoles());
        return "registro_usuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario";
        }
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "¡Usuario guardado con éxito!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario";
        }
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("usuario", usuario);
        return "ver_usuario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.getAllRoles());
        return "registro_usuario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el usuario.");
        }
        return "redirect:/usuarios";
    }
}