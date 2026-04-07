package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ejercicio_rutina")
public class EjercicioRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ejercicio_rutina_id")
    private Long ejercicioRutinaId;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false)
    private Integer repeticiones;

    private Double peso;  // Opcional, puede ser null

    private Integer descanso; // segundos entre series

    private Integer duracion; // duración en segundos (para ejercicios cardiovasculares)

    private Integer orden; // orden del ejercicio dentro de la rutina

    // ========== RELACIONES ==========

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rutina_id", nullable = false)
    private Rutina rutina;

    // ⚠️ IMPORTANTE: Relación con Exercise (NO crear nuevos ejercicios)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ejercicio_id", nullable = false)
    private Exercise ejercicio;  // Viene del catálogo (JSON)

    // ========== CONSTRUCTORES ==========

    public EjercicioRutina() {}

    public EjercicioRutina(Exercise ejercicio, Integer series, Integer repeticiones, Double peso, Integer orden) {
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.orden = orden;
    }

    // ========== GETTERS Y SETTERS ==========

    public Long getEjercicioRutinaId() {
        return ejercicioRutinaId;
    }

    public void setEjercicioRutinaId(Long ejercicioRutinaId) {
        this.ejercicioRutinaId = ejercicioRutinaId;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getDescanso() {
        return descanso;
    }

    public void setDescanso(Integer descanso) {
        this.descanso = descanso;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public Exercise getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Exercise ejercicio) {
        this.ejercicio = ejercicio;
    }
}