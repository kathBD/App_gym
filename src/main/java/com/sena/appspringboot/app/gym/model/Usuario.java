package com.sena.appspringboot.app.gym.model;

import com.fasterxml.jackson.annotation.JsonFormat; // Asegúrate de tener esta importación
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre es demasiado largo")
    private String nombre;

    @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo debe contener números")
    private String telefono;

    private String sexo;

    @Positive(message = "El peso debe ser un valor positivo")
    private Double peso;

    @Positive(message = "La estatura debe ser un valor positivo")
    private Double estatura;

    private Boolean activo = true;

    private String especialidad;
    private String horarioInicio;
    private String horarioFin;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha debe estar en el pasado")
    @JsonFormat(pattern = "yyyy-MM-dd") // Obliga a Jackson a usar este formato estándar
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_registro", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    @Size(max = 255, message = "El objetivo no puede superar los 255 caracteres")
    private String objetivo;

    @Column(name = "estado_fisico")
    private String estadoFisico;

    // Métodos (Getters, Setters y Edad) se mantienen igual...
    public Integer getEdad() {
        if (this.fechaNacimiento == null) return null;
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public @NotBlank(message = "El correo es obligatorio") @Email(message = "Debe ser un correo válido") String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotBlank(message = "El correo es obligatorio") @Email(message = "Debe ser un correo válido") String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password) {
        this.password = password;
    }

    public @NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre es demasiado largo") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre es demasiado largo") String nombre) {
        this.nombre = nombre;
    }

    public @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo debe contener números") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@Pattern(regexp = "^[0-9]*$", message = "El teléfono solo debe contener números") String telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public @Positive(message = "El peso debe ser un valor positivo") Double getPeso() {
        return peso;
    }

    public void setPeso(@Positive(message = "El peso debe ser un valor positivo") Double peso) {
        this.peso = peso;
    }

    public @Positive(message = "La estatura debe ser un valor positivo") Double getEstatura() {
        return estatura;
    }

    public void setEstatura(@Positive(message = "La estatura debe ser un valor positivo") Double estatura) {
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

    public @NotNull(message = "La fecha de nacimiento es obligatoria") @Past(message = "La fecha debe estar en el pasado") LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(@NotNull(message = "La fecha de nacimiento es obligatoria") @Past(message = "La fecha debe estar en el pasado") LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public @NotNull(message = "El rol es obligatorio") Rol getRol() {
        return rol;
    }

    public void setRol(@NotNull(message = "El rol es obligatorio") Rol rol) {
        this.rol = rol;
    }

    public @Size(max = 255, message = "El objetivo no puede superar los 255 caracteres") String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(@Size(max = 255, message = "El objetivo no puede superar los 255 caracteres") String objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstadoFisico() {
        return estadoFisico;
    }

    public void setEstadoFisico(String estadoFisico) {
        this.estadoFisico = estadoFisico;
    }
}
