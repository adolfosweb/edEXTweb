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

public class DTProgramaFormacion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaAlta;
    private String imagen;
    private List<String> cursos;
    private List<String> categorias;

    public DTProgramaFormacion(String nombre, String descripcion, LocalDate fechaInicio, 
                               LocalDate fechaFin, LocalDate fechaAlta, String imagen, 
                               List<String> cursos, List<String> categorias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAlta = fechaAlta;
        this.imagen = imagen;
        this.cursos = cursos;
        this.categorias = categorias;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public String getImagen() { return imagen; }
    public List<String> getCursos() { return cursos; }
    public List<String> getCategorias() { return categorias; }
}