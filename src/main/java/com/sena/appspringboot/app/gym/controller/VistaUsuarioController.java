package com.sena.appspringboot.app.gym.controller;


import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VistaUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String mostrarUsuarios(@RequestParam(value = "rol", required = false) String rol, Model model) {
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
}