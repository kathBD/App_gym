package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.*;
import com.sena.appspringboot.app.gym.repository.IEjercicioRutinaRepository;
import com.sena.appspringboot.app.gym.repository.ExerciseRepository;
import com.sena.appspringboot.app.gym.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rutinas")
@CrossOrigin(origins = "http://localhost:4200")
public class RutinaController {

    @Autowired private RutinaService rutinaService;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private IEjercicioRutinaRepository ejercicioRutinaRepository;

    // ── Helper ────────────────────────────────────────────────────────────────
    private Map<String, Object> rutinaToMap(Rutina rutina) {
        List<EjercicioRutina> ejercicios = ejercicioRutinaRepository
                .findByRutina_RutinaId(rutina.getRutinaId().longValue());

        List<Map<String, Object>> ejerciciosMap = ejercicios.stream().map(er -> {
            Map<String, Object> e = new HashMap<>();
            e.put("ejercicioRutinaId", er.getEjercicioRutinaId());
            e.put("ejercicioId",       er.getEjercicio().getId());
            e.put("nombre",            er.getEjercicio().getName());
            e.put("series",            er.getSeries());
            e.put("repeticiones",      er.getRepeticiones());
            e.put("descanso",          er.getDescanso());
            e.put("orden",             er.getOrden());
            e.put("peso",              er.getPeso());
            return e;
        }).collect(Collectors.toList());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("rutinaId",        rutina.getRutinaId());
        map.put("nombre",          rutina.getNombre());
        map.put("descripcion",     rutina.getDescripcion());
        map.put("objetivo",        rutina.getObjetivo());
        map.put("nivel",           rutina.getNivel());
        map.put("diaSemana",       rutina.getDiaSemana());
        map.put("duracionMinutos", rutina.getDuracionMinutos());
        map.put("creadorId",       rutina.getCreadorId());
        map.put("clienteId",       rutina.getClienteId());
        map.put("activo",          rutina.getActivo());
        map.put("estaActiva",      rutina.getEstaActiva());
        map.put("fechaCreacion",   rutina.getFechaCreacion());
        map.put("ejercicios",      ejerciciosMap);
        return map;
    }

    private void guardarEjercicios(Rutina rutina, Map<String, Object> datos) {
        List<Map<String, Object>> ejercicios =
                (List<Map<String, Object>>) datos.get("ejercicios");
        if (ejercicios == null || ejercicios.isEmpty()) return;

        for (Map<String, Object> ej : ejercicios) {
            String ejercicioId = (String) ej.get("ejercicioId");
            Exercise exercise = exerciseRepository.findById(ejercicioId)
                    .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado: " + ejercicioId));

            EjercicioRutina er = new EjercicioRutina();
            er.setRutina(rutina);
            er.setEjercicio(exercise);
            er.setSeries(((Number) ej.get("series")).intValue());
            er.setRepeticiones(((Number) ej.get("repeticiones")).intValue());
            er.setOrden(((Number) ej.get("orden")).intValue());
            er.setDescanso(ej.containsKey("descanso") && ej.get("descanso") != null
                    ? ((Number) ej.get("descanso")).intValue() : 60);
            er.setPeso(ej.get("peso") != null
                    ? ((Number) ej.get("peso")).doubleValue() : 0.0);

            ejercicioRutinaRepository.save(er);
        }
    }

    // ── GET todas ─────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodas(
            @RequestParam(required = false) Long creadorId) {
        List<Rutina> rutinas = creadorId != null
                ? rutinaService.obtenerPorCreador(creadorId)
                : rutinaService.listarTodas();
        return ResponseEntity.ok(rutinas.stream()
                .map(this::rutinaToMap).collect(Collectors.toList()));
    }

    // ── GET por ID ────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        return rutinaService.obtenerPorId(id)
                .map(r -> ResponseEntity.ok(rutinaToMap(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET por creador ───────────────────────────────────────────────────────
    @GetMapping("/creador/{creadorId}")
    public ResponseEntity<List<Map<String, Object>>> obtenerPorCreador(
            @PathVariable Long creadorId) {
        return ResponseEntity.ok(rutinaService.obtenerPorCreador(creadorId)
                .stream().map(this::rutinaToMap).collect(Collectors.toList()));
    }

    // ── GET por cliente ───────────────────────────────────────────────────────
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Map<String, Object>>> obtenerPorCliente(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(rutinaService.obtenerPorCliente(clienteId)
                .stream().map(this::rutinaToMap).collect(Collectors.toList()));
    }

    // ── POST crear ────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Rutina> crearRutina(@RequestBody Rutina rutina) {
        try {
            return new ResponseEntity<>(rutinaService.crearRutina(rutina), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ── POST guardar con ejercicios ───────────────────────────────────────────
    @PostMapping("/guardar-con-ejercicios")
    public ResponseEntity<?> guardarRutinaConEjercicios(
            @RequestBody Map<String, Object> datos) {
        try {
            Rutina rutina = new Rutina();
            rutina.setNombre((String) datos.get("nombre"));
            rutina.setDescripcion((String) datos.get("descripcion"));
            rutina.setObjetivo((String) datos.get("objetivo"));
            rutina.setNivel((String) datos.get("nivel"));
            rutina.setActivo(true);
            rutina.setEstaActiva(true);

            if (datos.get("duracionMinutos") != null)
                rutina.setDuracionMinutos(((Number) datos.get("duracionMinutos")).intValue());
            if (datos.get("diaSemana") != null)
                rutina.setDiaSemana((String) datos.get("diaSemana"));

            Long creadorId = ((Number) datos.get("creadorId")).longValue();
            rutina.setCreadorId(creadorId);
            rutina.setUsuarioId(creadorId);

            if (datos.get("clienteId") != null && !datos.get("clienteId").toString().equals(""))
                rutina.setClienteId(((Number) datos.get("clienteId")).longValue());

            Rutina guardada = rutinaService.crearRutina(rutina);
            guardarEjercicios(guardada, datos);
            return ResponseEntity.ok(rutinaToMap(guardada));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // ── PUT actualizar con ejercicios ─────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRutinaConEjercicios(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        try {
            Rutina rutina = rutinaService.obtenerPorId(id)
                    .orElseThrow(() -> new RuntimeException("Rutina no encontrada: " + id));

            rutina.setNombre((String) datos.get("nombre"));
            rutina.setDescripcion((String) datos.get("descripcion"));
            rutina.setObjetivo((String) datos.get("objetivo"));
            rutina.setNivel((String) datos.get("nivel"));

            if (datos.get("duracionMinutos") != null)
                rutina.setDuracionMinutos(((Number) datos.get("duracionMinutos")).intValue());
            if (datos.get("diaSemana") != null)
                rutina.setDiaSemana((String) datos.get("diaSemana"));

            if (datos.get("clienteId") != null && !datos.get("clienteId").toString().equals(""))
                rutina.setClienteId(((Number) datos.get("clienteId")).longValue());
            else
                rutina.setClienteId(null);

            Rutina actualizada = rutinaService.actualizarRutina(id, rutina);

            List<EjercicioRutina> viejos = ejercicioRutinaRepository.findByRutina_RutinaId(id);
            ejercicioRutinaRepository.deleteAll(viejos);
            guardarEjercicios(actualizada, datos);

            return ResponseEntity.ok(rutinaToMap(actualizada));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // ── PUT asignar cliente ───────────────────────────────────────────────────
    @PutMapping("/{id}/asignar-cliente/{clienteId}")
    public ResponseEntity<?> asignarCliente(
            @PathVariable Long id,
            @PathVariable Long clienteId) {
        try {
            Rutina rutina = rutinaService.obtenerPorId(id)
                    .orElseThrow(() -> new RuntimeException("Rutina no encontrada: " + id));

            rutina.setClienteId(clienteId);
            rutinaService.actualizarRutina(id, rutina);

            Rutina actualizada = rutinaService.obtenerPorId(id).get();
            return ResponseEntity.ok(rutinaToMap(actualizada));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable Long id) {
        try {
            if (!rutinaService.obtenerPorId(id).isPresent())
                return ResponseEntity.notFound().build();

            List<EjercicioRutina> ejercicios = ejercicioRutinaRepository.findByRutina_RutinaId(id);
            if (!ejercicios.isEmpty())
                ejercicioRutinaRepository.deleteAll(ejercicios);

            rutinaService.eliminarRutina(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ── Ejercicios disponibles ────────────────────────────────────────────────
    @GetMapping("/ejercicios-disponibles")
    public ResponseEntity<List<Exercise>> obtenerEjerciciosDisponibles() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }

    @GetMapping("/ejercicios")
    public ResponseEntity<List<Exercise>> getEjercicios() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }

    @PostMapping("/con-ejercicios")
    public ResponseEntity<?> crearRutinaConEjercicios(
            @RequestBody Map<String, Object> request) {
        return guardarRutinaConEjercicios(request);
    }
}