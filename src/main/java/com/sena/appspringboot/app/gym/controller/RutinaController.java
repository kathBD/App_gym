package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.Rutina;
import com.sena.appspringboot.app.gym.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
@CrossOrigin(origins = "http://localhost:4200")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    // ========== CRUD BÁSICO ==========

    @PostMapping
    public ResponseEntity<Rutina> crearRutina(@RequestBody Rutina rutina) {
        try {
            System.out.println("Recibiendo rutina: " + rutina.getNombre());
            Rutina nueva = rutinaService.crearRutina(rutina);
            return new ResponseEntity<>(nueva, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Rutina>> listarTodas() {
        return ResponseEntity.ok(rutinaService.listarTodas());
    }

    //  Obtener rutina por ID
    @GetMapping("/{id}")
    public ResponseEntity<Rutina> obtenerPorId(@PathVariable Long id) {
        return rutinaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener rutinas por creador (entrenador)
    @GetMapping("/creador/{creadorId}")
    public ResponseEntity<List<Rutina>> obtenerPorCreador(@PathVariable Long creadorId) {
        return ResponseEntity.ok(rutinaService.obtenerPorCreador(creadorId));
    }

    // Obtener rutinas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Rutina>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(rutinaService.obtenerPorCliente(clienteId));
    }

    // Asignar rutina a cliente
    @PutMapping("/{rutinaId}/asignar-cliente/{clienteId}")
    public ResponseEntity<Rutina> asignarACliente(@PathVariable Long rutinaId, @PathVariable Long clienteId) {
        Rutina rutina = rutinaService.asignarACliente(rutinaId, clienteId);
        if (rutina != null) {
            return ResponseEntity.ok(rutina);
        }
        return ResponseEntity.notFound().build();
    }

    // Editar rutina
    @PutMapping("/{id}")
    public ResponseEntity<Rutina> actualizarRutina(@PathVariable Long id, @RequestBody Rutina rutina) {
        Rutina actualizada = rutinaService.actualizarRutina(id, rutina);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    //Eliminar rutina
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable Long id) {
        rutinaService.eliminarRutina(id);
        return ResponseEntity.noContent().build();
    }
}