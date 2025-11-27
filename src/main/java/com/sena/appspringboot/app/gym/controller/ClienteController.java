package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Rutina;
import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.service.RutinaService;
import com.sena.appspringboot.app.gym.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/cliente")
public class ClienteController {



        @Autowired
        private RutinaService rutinaService;

        @Autowired
        private UsuarioService usuarioService;

        @GetMapping("")
        public String dashboardCliente() {
            return "cliente";
        }

        @GetMapping("/rutinas")
        public String mostrarRutinasCliente(Model model) {
            List<Rutina> rutinas = rutinaService.listarTodas(); // luego puedes filtrar por usuario
            model.addAttribute("rutinas", rutinas);
            return "/rutinas";
        }
        @GetMapping("/perfil")
        public String mostrarPerfilCliente(Model model, Principal principal) {
            // Obtener el correo del usuario autenticado
            String correo = principal.getName();

            // Buscar el usuario por correo
            Usuario usuario = usuarioService.findByCorreo(correo);

            // Pasar el usuario a la vista
            model.addAttribute("usuario", usuario);

            return "perfil";
        }


    }


