/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datatypes;

/**
 *
 * @author adolfo
 */

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class DTEdicionCurso implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupo;
    private LocalDate fechaPublicacion;
    private String curso;
    private String imagen;
    private List<String> docentes;

    public DTEdicionCurso(String nombre, LocalDate fechaInicio, LocalDate fechaFin, 
                          int cupo, LocalDate fechaPublicacion, String curso, 
                          String imagen, List<String> docentes) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.fechaPublicacion = fechaPublicacion;
        this.curso = curso;
        this.imagen = imagen;
        this.docentes = docentes;
    }

    public String getNombre() { return nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getCupo() { return cupo; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public String getCurso() { return curso; }
    public String getImagen() { return imagen; }
    public List<String> getDocentes() { return docentes; }
}