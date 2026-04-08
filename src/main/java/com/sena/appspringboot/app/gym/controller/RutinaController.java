package com.sena.appspringboot.app.gym.controller;

import com.sena.appspringboot.app.gym.model.*;
import com.sena.appspringboot.app.gym.repository.IEjercicioRutinaRepository;
import com.sena.appspringboot.app.gym.repository.ExerciseRepository;
import com.sena.appspringboot.app.gym.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rutinas")
@CrossOrigin(origins = "http://localhost:4200")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private IEjercicioRutinaRepository ejercicioRutinaRepository;

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

    // Crear rutina con ejercicios
    @PostMapping("/con-ejercicios")
    public ResponseEntity<?> crearRutinaConEjercicios(@RequestBody Map<String, Object> request) {
        try {
            // Extraer datos básicos de la rutina
            Rutina rutina = new Rutina();
            rutina.setNombre((String) request.get("nombre"));
            rutina.setDescripcion((String) request.get("descripcion"));
            rutina.setObjetivo((String) request.get("objetivo"));
            rutina.setNivel((String) request.get("nivel"));
            rutina.setActivo(true);
            rutina.setEstaActiva(true);

            if (request.containsKey("duracionMinutos")) {
                rutina.setDuracionMinutos((Integer) request.get("duracionMinutos"));
            }
            if (request.containsKey("diaSemana")) {
                rutina.setDiaSemana((String) request.get("diaSemana"));
            }
            if (request.containsKey("creadorId")) {
                rutina.setCreadorId(((Number) request.get("creadorId")).longValue());
            }
            if (request.containsKey("clienteId") && request.get("clienteId") != null) {
                rutina.setClienteId(((Number) request.get("clienteId")).longValue());
            }

            // Guardar la rutina primero
            Rutina rutinaGuardada = rutinaService.crearRutina(rutina);

            // Procesar los ejercicios
            List<Map<String, Object>> ejercicios = (List<Map<String, Object>>) request.get("ejercicios");
            if (ejercicios != null && !ejercicios.isEmpty()) {
                for (Map<String, Object> ejercicioData : ejercicios) {
                    String ejercicioId = (String) ejercicioData.get("ejercicioId");
                    Exercise exercise = exerciseRepository.findById(ejercicioId)
                            .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado: " + ejercicioId));

                    EjercicioRutina er = new EjercicioRutina();
                    er.setRutina(rutinaGuardada);
                    er.setEjercicio(exercise);
                    er.setSeries(((Number) ejercicioData.get("series")).intValue());
                    er.setRepeticiones(((Number) ejercicioData.get("repeticiones")).intValue());
                    er.setOrden(((Number) ejercicioData.get("orden")).intValue());
                    er.setDescanso(ejercicioData.containsKey("descanso") ? ((Number) ejercicioData.get("descanso")).intValue() : 60);

                    ejercicioRutinaRepository.save(er);
                }
            }

            return ResponseEntity.ok(rutinaGuardada);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la rutina: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Rutina>> listarTodas(@RequestParam(required = false) Long creadorId) {
        if (creadorId != null) {
            return ResponseEntity.ok(rutinaService.obtenerPorCreador(creadorId));
        }
        return ResponseEntity.ok(rutinaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return rutinaService.obtenerPorId(id)
                .map(rutina -> {
                    // También cargar los ejercicios
                    List<EjercicioRutina> ejercicios = ejercicioRutinaRepository.findByRutina_RutinaId(id);
                    return ResponseEntity.ok(rutina);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Endpoint para obtener ejercicios disponibles
    @GetMapping("/ejercicios-disponibles")
    public ResponseEntity<List<Exercise>> obtenerEjerciciosDisponibles() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }

    // 1. Método para obtener ejercicios disponibles (usando tu ExerciseRepository)
    @GetMapping("/ejercicios")
    public ResponseEntity<List<Exercise>> getEjercicios() {
        try {
            List<Exercise> ejercicios = exerciseRepository.findAll();
            System.out.println("Ejercicios encontrados: " + ejercicios.size());
            return ResponseEntity.ok(ejercicios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 2. Método para guardar rutina con ejercicios
    @PostMapping("/guardar-con-ejercicios")
    public ResponseEntity<?> guardarRutinaConEjercicios(@RequestBody Map<String, Object> datos) {
        try {

            System.out.println("========== GUARDANDO RUTINA ==========");
            System.out.println("📦 Datos recibidos: " + datos);
            System.out.println("🔑 creadorId en datos: " + datos.get("creadorId"));
            // Crear la rutina
            Rutina rutina = new Rutina();
            rutina.setNombre((String) datos.get("nombre"));
            rutina.setDescripcion((String) datos.get("descripcion"));
            rutina.setObjetivo((String) datos.get("objetivo"));
            rutina.setNivel((String) datos.get("nivel"));
            rutina.setActivo(true);

            Long creadorId = ((Number) datos.get("creadorId")).longValue();
            rutina.setCreadorId(creadorId);
            rutina.setUsuarioId(creadorId);
            // Guardar rutina
            Rutina rutinaGuardada = rutinaService.crearRutina(rutina);

            // Guardar ejercicios
// Guardar ejercicios
            List<Map<String, Object>> ejercicios = (List<Map<String, Object>>) datos.get("ejercicios");
            if (ejercicios != null) {
                for (Map<String, Object> ej : ejercicios) {
                    String ejercicioId = (String) ej.get("ejercicioId");
                    Exercise exercise = exerciseRepository.findById(ejercicioId).orElseThrow();

                    EjercicioRutina er = new EjercicioRutina();
                    er.setRutina(rutinaGuardada);
                    er.setEjercicio(exercise);
                    er.setSeries((Integer) ej.get("series"));
                    er.setRepeticiones((Integer) ej.get("repeticiones"));
                    er.setOrden((Integer) ej.get("orden"));
                    er.setDescanso((Integer) ej.getOrDefault("descanso", 60));
                    // CORRECCIÓN: Manejar peso que puede venir como Integer o Double
                    Object pesoValue = ej.get("peso");
                    if (pesoValue != null) {
                        if (pesoValue instanceof Integer) {
                            er.setPeso(((Integer) pesoValue).doubleValue());
                        } else if (pesoValue instanceof Double) {
                            er.setPeso((Double) pesoValue);
                        } else if (pesoValue instanceof Number) {
                            er.setPeso(((Number) pesoValue).doubleValue());
                        } else {
                            er.setPeso(0.0);
                        }
                    } else {
                        er.setPeso(0.0);
                    }

                    ejercicioRutinaRepository.save(er);
                }
            }

            return ResponseEntity.ok(rutinaGuardada);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/creador/{creadorId}")
    public ResponseEntity<List<Rutina>> obtenerPorCreador(@PathVariable Long creadorId) {
        try {
            System.out.println("🔍 Buscando rutinas del creador: " + creadorId);
            List<Rutina> rutinas = rutinaService.obtenerPorCreador(creadorId);
            System.out.println("📊 Encontradas: " + rutinas.size());
            return ResponseEntity.ok(rutinas);
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Eliminando rutina con ID: " + id);

            // Verificar si la rutina existe
            var rutinaOpt = rutinaService.obtenerPorId(id);
            if (!rutinaOpt.isPresent()) {
                System.out.println("❌ Rutina no encontrada con ID: " + id);
                return ResponseEntity.notFound().build();
            }

            // Primero eliminar los ejercicios asociados (foreign key)
            List<EjercicioRutina> ejercicios = ejercicioRutinaRepository.findByRutina_RutinaId(id);
            if (!ejercicios.isEmpty()) {
                ejercicioRutinaRepository.deleteAll(ejercicios);
                System.out.println("✅ Eliminados " + ejercicios.size() + " ejercicios asociados");
            }

            // Eliminar la rutina
            rutinaService.eliminarRutina(id);
            System.out.println("✅ Rutina eliminada correctamente");

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar rutina: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}