package com.sena.appspringboot.app.gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "usuarios")
public class Usuario {

    // ID de usuario, con estrategia de generación de valor automático (IDENTITY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    // Validación: El correo es obligatorio y debe ser un correo válido
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    @Column(nullable = false, unique = true)
    private String correo;

    // Validación: La contraseña es obligatoria y debe tener un mínimo de 6 caracteres
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Validación: El nombre es obligatorio
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    // Otros campos opcionales para el usuario
    private String telefono;
    private String sexo;
    private Double peso;
    private Double estatura;

    // Flag para saber si el usuario está activo. Por defecto es true
    private Boolean activo = true;

    private String especialidad;
    private String horarioInicio;
    private String horarioFin;

    // Validación: La fecha de nacimiento debe estar en el pasado
    @Past(message = "La fecha debe estar en el pasado")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // Fecha de registro automática cuando se inserta el usuario
    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // Relación ManyToOne con la tabla "roles", un usuario tiene un rol asignado
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // Campo adicional para el objetivo del usuario (si lo tiene)
    @Size(max = 255)
    private String objetivo;

    // Estado físico del usuario
    @Column(name = "estado_fisico")
    private String estadoFisico;

    // Método para calcular la edad del usuario en base a su fecha de nacimiento
    public Integer getEdad() {
        if (this.fechaNacimiento == null) {
            return null;
        }
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    // Getters y Setters para todos los atributos

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(String horarioFin) {
        this.horarioFin = horarioFin;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstadoFisico() {
        return estadoFisico;
    }

    public void setEstadoFisico(String estadoFisico) {
        this.estadoFisico = estadoFisico;
    }
}
