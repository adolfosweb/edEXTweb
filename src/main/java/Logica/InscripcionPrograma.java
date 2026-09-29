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
public class InscripcionPrograma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInscripcion;

    @ManyToOne
    private Estudiante estudianteInscripto;

    @ManyToOne
    private ProgramaFormacion programaInscripto;

    public InscripcionPrograma() {
    }

    public InscripcionPrograma(
            Estudiante es,
            LocalDate fecIns,
            ProgramaFormacion pf) {

        this.estudianteInscripto = es;
        this.fechaInscripcion = fecIns;
        this.programaInscripto = pf;
    }

    public Long getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudianteInscripto;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudianteInscripto = estudiante;
    }

    public LocalDate getFechaIns() {
        return fechaInscripcion;
    }

    public void setFechaIns(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public ProgramaFormacion getPrograma() {
        return programaInscripto;
    }

    public void setPrograma(ProgramaFormacion programa) {
        this.programaInscripto = programa;
    }
}
