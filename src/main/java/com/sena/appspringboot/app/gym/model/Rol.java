package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rolId;

    private String nombre;
    private String descripcion;

    // Getters y Setters

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
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

    // Método @PrePersist que garantiza que el nombre del rol tendrá el prefijo "ROLE_"
    @PrePersist
    private void asignarPrefijo() {
        if (nombre != null && !nombre.startsWith("ROLE_")) {
            nombre = "ROLE_" + nombre; // Agrega el prefijo ROLE_ para cumplir con las convenciones de Spring Security
        }
    }
}

