package com.sena.appspringboot.app.gym.model;
import jakarta.persistence.*;

@Entity
@Table(name = "ejercicio_rutina")
public class EjercicioRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ejercicioRutinaId;

    private int series;
    private int repeticiones;
    private int peso;
    private int descanso;
    private int duracion;

    @ManyToOne
    @JoinColumn(name = "rutina_id")
    private Rutina rutina;

    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    private Ejercicio ejercicio;

    // Getters y Setters

    public Long getEjercicioRutinaId() {
        return ejercicioRutinaId;
    }

    public void setEjercicioRutinaId(Long ejercicioRutinaId) {
        this.ejercicioRutinaId = ejercicioRutinaId;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getDescanso() {
        return descanso;
    }

    public void setDescanso(int descanso) {
        this.descanso = descanso;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }
}
