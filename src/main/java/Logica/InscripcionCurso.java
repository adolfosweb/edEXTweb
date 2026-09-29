/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class InscripcionCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInscripcion;

    @ManyToOne
    private Estudiante estudiante;

    @ManyToOne
    private EdicionCurso edicionCurso;

    public InscripcionCurso() {
    }

    public InscripcionCurso(Estudiante estudiante,
            LocalDate fechaInscripcion,
            EdicionCurso curso) {

        this.estudiante = estudiante;
        this.fechaInscripcion = fechaInscripcion;
        this.edicionCurso = curso;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaIns() {
        return fechaInscripcion;
    }

    public void setFechaIns(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {

        this.estudiante = estudiante;

        if (estudiante != null
                && !estudiante.getCursos().contains(this)) {

            estudiante.getCursos().add(this);
        }
    }

    public EdicionCurso getCurso() {
        return edicionCurso;
    }

    public void setCurso(EdicionCurso curso) {

        this.edicionCurso = curso;

        if (curso != null
                && !curso.getInscriptos().contains(this)) {

            curso.getInscriptos().add(this);
        }
    }
}