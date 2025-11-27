package com.sena.appspringboot.app.gym.service;


import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.repository.IRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    @Autowired
    private IRolRepository rolRepository;

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    public Rol getRolById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }


}
