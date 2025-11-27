package com.sena.appspringboot.app.gym.service;

import com.sena.appspringboot.app.gym.model.Rutina;
import com.sena.appspringboot.app.gym.repository.IRutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutinaService {

    @Autowired
    private IRutinaRepository rutinaRepository;

    public List<Rutina> obtenerRutinasPorUsuario(Long usuarioId) {
        return rutinaRepository.findByUsuarioUsuarioId(usuarioId);
    }

    //  listar todas las rutinas
    public List<Rutina> listarTodas() {
        return rutinaRepository.findAll();
    }
}


