package com.sena.appspringboot.app.gym.controller;


import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public List<Rol> obtenerRoles() {
        return rolService.getAllRoles();
    }
}
