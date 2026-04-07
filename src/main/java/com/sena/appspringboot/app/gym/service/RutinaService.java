package com.sena.appspringboot.app.gym.service;

import com.sena.appspringboot.app.gym.model.Rutina;
import com.sena.appspringboot.app.gym.repository.IRutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RutinaService {

    @Autowired
    private IRutinaRepository rutinaRepository;

    // ========== CRUD BÁSICO ==========

    public Rutina crearRutina(Rutina rutina) {
        rutina.setFechaCreacion(LocalDateTime.now());
        rutina.setFechaModificacion(LocalDateTime.now());
        if (rutina.getActivo() == null) rutina.setActivo(true);
        if (rutina.getEstaActiva() == null) rutina.setEstaActiva(true);
        if (rutina.getNivel() == null) rutina.setNivel("principiante");
        if (rutina.getObjetivo() == null || rutina.getObjetivo().isEmpty()) {
            rutina.setObjetivo("general");
        }
        return rutinaRepository.save(rutina);
    }

    public List<Rutina> listarTodas() {
        return rutinaRepository.findAll();
    }

    public Optional<Rutina> obtenerPorId(Long id) {
        return rutinaRepository.findById(id);
    }

    // Obtener rutinas por creador (entrenador)
    public List<Rutina> obtenerPorCreador(Long creadorId) {
        return rutinaRepository.findByCreadorId(creadorId);
    }

    //  Obtener rutinas por cliente
    public List<Rutina> obtenerPorCliente(Long clienteId) {
        return rutinaRepository.findByClienteId(clienteId);
    }

    // Asignar rutina a cliente
    public Rutina asignarACliente(Long rutinaId, Long clienteId) {
        Optional<Rutina> optional = rutinaRepository.findById(rutinaId);
        if (optional.isPresent()) {
            Rutina rutina = optional.get();
            rutina.setClienteId(clienteId);
            return rutinaRepository.save(rutina);
        }
        return null;
    }

    // Actualizar rutina
    public Rutina actualizarRutina(Long id, Rutina rutinaActualizada) {
        Optional<Rutina> optional = rutinaRepository.findById(id);
        if (optional.isPresent()) {
            Rutina rutina = optional.get();
            rutina.setNombre(rutinaActualizada.getNombre());
            rutina.setDescripcion(rutinaActualizada.getDescripcion());
            rutina.setNivel(rutinaActualizada.getNivel());
            rutina.setObjetivo(rutinaActualizada.getObjetivo());
            rutina.setActivo(rutinaActualizada.getActivo());
            rutina.setEstaActiva(rutinaActualizada.getEstaActiva());
            rutina.setFechaModificacion(LocalDateTime.now());
            return rutinaRepository.save(rutina);
        }
        return null;
    }

    // Eliminar rutina
    public void eliminarRutina(Long id) {
        rutinaRepository.deleteById(id);
    }
}