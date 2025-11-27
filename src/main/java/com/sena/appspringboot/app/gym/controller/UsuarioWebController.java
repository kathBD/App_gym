package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.RolService;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inyectar el PasswordEncoder

    // Mostrar el formulario para registrar un nuevo usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        List<Rol> roles = rolService.getAllRoles();
        Usuario usuario = new Usuario();
        usuario.setRol(new Rol()); // Establecer un rol vacío para evitar problemas con el formulario

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);

        return "registro_usuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        // Verifica si el correo ya está registrado
        // Verificar si el correo ya está registrado
        if (usuarioService.findByCorreo(usuario.getCorreo()) != null && usuario.getUsuarioId() == null) {
            model.addAttribute("error", "Usuario ya registrado.");
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario"; // Retorna al formulario si el correo ya está registrado
        }


        try {
            // Encriptar la contraseña antes de guardar
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));  // Usando el PasswordEncoder inyectado
            usuarioService.guardarUsuario(usuario);

            // Añadir el mensaje de éxito al objeto RedirectAttributes
            redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado correctamente.");

            // Redirigir a la lista de usuarios
            return "redirect:/usuarios"; // Redirige a la lista de usuarios
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el usuario: " + e.getMessage());
            model.addAttribute("roles", rolService.getAllRoles());
            return "registro_usuario"; // Si hay un error, regresa al formulario
        }
    }

    // Método para ver los detalles del usuario
    @GetMapping("/ver/{id}")
    public String mostrarUsuario(@PathVariable Long id, Model model) {
        // Obtener el usuario por ID
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        // Agregar el usuario al modelo
        model.addAttribute("usuario", usuario);

        // Devolver la vista para mostrar el usuario
        return "ver_usuario";  // Asegúrate de que la vista "ver_usuario.html" exista
    }

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') or #id == authentication.principal.id")
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        try {
            // Obtener el usuario por ID
            Usuario usuario = usuarioService.obtenerPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

            // Pasar el usuario y los roles al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", rolService.getAllRoles());

            // Devolver la vista de edición
            return "editar_usuario";  // Asegúrate de que la vista "editar_usuario.html" exista
        } catch (Exception e) {
            // Logueamos el error para depuración
            System.out.println("Error al intentar editar el usuario: " + e.getMessage());
            model.addAttribute("error", "Error al editar el usuario: " + e.getMessage());
            return "error";  // Devolvemos una vista de error si algo sale mal
        }
    }


    // Método para eliminar un usuario
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario.");
        }
        return "redirect:/usuarios";
    }

}
