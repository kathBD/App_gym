package com.sena.appspringboot.app.gym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @GetMapping("/inicio")
    public String home() {
        return "inicio";
    }


    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @RequestMapping("/cliente/dashboard")
    public class ClienteController {
        @GetMapping("")
        public String dashboardCliente() {
            return "cliente";
        }
    }


    @GetMapping("/entrenador")
    public String entrenadorPage() {
        return "entrenador";
    }
}
