/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EdicionCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String nombre;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupo;
    private LocalDate fechaPublicacion;

    // Muchas ediciones pertenecen a un curso
    @ManyToOne
    private Curso curso;

    // Muchos docentes pueden participar en muchas ediciones
    @ManyToMany
    private List<Docente> docentes = new ArrayList<>();

    // Una edición tiene muchas inscripciones
    //    @OneToMany(mappedBy = "edicionCurso")
    @OneToMany(mappedBy = "edicionCurso", fetch = jakarta.persistence.FetchType.EAGER)
    private List<InscripcionCurso> inscriptos = new ArrayList<>();

    public EdicionCurso() {
    }

    public EdicionCurso(String nombre,
            LocalDate fecInicio,
            LocalDate fecFin,
            int cupo,
            LocalDate fecPublicacion,
            Curso curso,
            Docente docente) {

        this.nombre = nombre;
        this.fechaInicio = fecInicio;
        this.fechaFin = fecFin;
        this.cupo = cupo;
        this.fechaPublicacion = fecPublicacion;
        this.curso = curso;

        if (docente != null) {
            this.docentes.add(docente);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fecInicio) {
        this.fechaInicio = fecInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fecFin) {
        this.fechaFin = fecFin;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fecPubli) {
        this.fechaPublicacion = fecPubli;
    }

    // =========================
    // CURSO
    // =========================

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {

        this.curso = curso;

        if (curso != null && !curso.getEdiciones().contains(this)) {
            curso.getEdiciones().add(this);
        }
    }

    // =========================
    // DOCENTES
    // =========================

    public List<Docente> getDocentes() {
        return docentes;
    }

    public void agregarDocente(Docente d) {

        if (d != null && !docentes.contains(d)) {

            docentes.add(d);

            if (!d.getEdiciones().contains(this)) {
                d.getEdiciones().add(this);
            }
        }
    }

    public void eliminarDocente(Docente d) {

        if (d != null) {

            docentes.remove(d);

            if (d.getEdiciones().contains(this)) {
                d.getEdiciones().remove(this);
            }
        }
    }

    // =========================
    // INSCRIPCIONES
    // =========================

    public List<InscripcionCurso> getInscriptos() {
        return inscriptos;
    }

    public void agregarInscripto(InscripcionCurso ic) {

        if (ic != null && !inscriptos.contains(ic)) {

            inscriptos.add(ic);

            if (ic.getCurso() != this) {
                ic.setCurso(this);
            }
        }
    }

    public void eliminarInscripto(InscripcionCurso ic) {

        if (ic != null) {

            inscriptos.remove(ic);

            if (ic.getCurso() == this) {
                ic.setCurso(null);
            }
        }
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof EdicionCurso)) {
            return false;
        }

        EdicionCurso other = (EdicionCurso) obj;

        if (nombre == null && other.nombre != null) {
            return false;
        }

        return nombre != null && nombre.equals(other.nombre);
    }
}