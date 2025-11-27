package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_ejercicio")
public class TipoEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tipoId;

    private String descripcion;

    // Getters y Setters

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
