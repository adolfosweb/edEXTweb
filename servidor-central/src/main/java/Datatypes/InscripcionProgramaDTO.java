package Datatypes;

import java.io.Serializable;

public class InscripcionProgramaDTO implements Serializable {

    private String nombrePrograma;
    private String fechaInscripcion;
    private String estado;

    public InscripcionProgramaDTO() {
    }

    public InscripcionProgramaDTO(String nombrePrograma,String fechaInscripcion,String estado) {
        this.nombrePrograma = nombrePrograma;
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
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