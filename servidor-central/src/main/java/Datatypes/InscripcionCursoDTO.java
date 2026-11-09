package Datatypes;

import java.io.Serializable;

public class InscripcionCursoDTO implements Serializable {

    private String nombreEdicion;
    private String nombreCurso;
    private String fechaInscripcion;
    private String estado;

    public InscripcionCursoDTO() {
    }

    public InscripcionCursoDTO(String nombreEdicion,String nombreCurso,String fechaInscripcion,String estado) {

        this.nombreEdicion = nombreEdicion;
        this.nombreCurso = nombreCurso;
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public void setNombreEdicion(String nombreEdicion) {
        this.nombreEdicion = nombreEdicion;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}