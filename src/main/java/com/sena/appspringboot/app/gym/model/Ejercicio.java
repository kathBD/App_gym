package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;


@Entity
@Table(name = "ejercicios")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ejercicioId;

    private String nombre;
    private String descripcion;
    private String videoUrl;
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoEjercicio tipo;

    // Getters y Setters

    public Long getEjercicioId() {
        return ejercicioId;
    }

    public void setEjercicioId(Long ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public TipoEjercicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoEjercicio tipo) {
        this.tipo = tipo;
    }
}
