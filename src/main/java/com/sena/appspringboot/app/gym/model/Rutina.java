package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rutina_id")
    private Integer rutinaId;

    private String nombre;
    private String descripcion;
    private Integer series;
    private Integer repeticiones;

    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "esta_activa")
    private Boolean estaActiva;

    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    private String nivel;
    private String objetivo;
    private Boolean activo;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "creador_id")
    private Long creadorId;

    // ========== CONSTRUCTORES ==========
    public Rutina() {}

    // ========== MÉTODOS DE AUDITORÍA ==========
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
        if (activo == null) activo = true;
        if (estaActiva == null) estaActiva = true;
        if (nivel == null) nivel = "principiante";
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    // ========== GETTERS Y SETTERS ==========
    public Integer getRutinaId() { return rutinaId; }
    public void setRutinaId(Integer rutinaId) { this.rutinaId = rutinaId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getSeries() { return series; }
    public void setSeries(Integer series) { this.series = series; }

    public Integer getRepeticiones() { return repeticiones; }
    public void setRepeticiones(Integer repeticiones) { this.repeticiones = repeticiones; }

    public Integer getPlanId() { return planId; }
    public void setPlanId(Integer planId) { this.planId = planId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Boolean getEstaActiva() { return estaActiva; }
    public void setEstaActiva(Boolean estaActiva) { this.estaActiva = estaActiva; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getCreadorId() { return creadorId; }
    public void setCreadorId(Long creadorId) { this.creadorId = creadorId; }
}