/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProgramaFormacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String nombre;

    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaAlta;

    @ManyToMany
    private final List<Curso> cursos = new ArrayList<>();

    @OneToMany(mappedBy = "programaInscripto")
    private final List<InscripcionPrograma> inscriptos = new ArrayList<>();

    public ProgramaFormacion() {
    }

    public ProgramaFormacion(String nombre, String descripcion,
            LocalDate fechaInicio, LocalDate fechaFin) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = LocalDate.now();
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public boolean agregarCurso(Curso c) {
        if (c != null && !cursos.contains(c)) {
            cursos.add(c);
            return true;
        }
        return false;
    }

    public void eliminarCurso(Curso c) {
        cursos.remove(c);
    }

    public List<InscripcionPrograma> getInscriptos() {
        return inscriptos;
    }

    public void agregarInscripto(InscripcionPrograma ip) {
        if (ip != null && !inscriptos.contains(ip)) {
            inscriptos.add(ip);
        }
    }

    public void eliminarInscripto(InscripcionPrograma ip) {
        inscriptos.remove(ip);
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProgramaFormacion)) {
            return false;
        }

        ProgramaFormacion other = (ProgramaFormacion) obj;

        if (nombre == null && other.nombre != null) {
            return false;
        }

        if (nombre != null && !nombre.equals(other.nombre)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
