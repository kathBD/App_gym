package com.sena.appspringboot.app.gym.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    public String dashboard() {
        // Esto devolverá el archivo admin_dashboard.html que está en templates
        return "admin_dashboard";
    }

}
