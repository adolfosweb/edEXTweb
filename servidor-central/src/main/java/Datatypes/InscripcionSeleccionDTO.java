package Datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InscripcionSeleccionDTO")
public class InscripcionSeleccionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private UsuarioDTO estudiante;
    private String fechaInscripcion;
    private String estado;

    public InscripcionSeleccionDTO() {
    }

    public InscripcionSeleccionDTO(Long id, UsuarioDTO estudiante,
            String fechaInscripcion, String estado) {
        this.id = id;
        this.estudiante = estudiante;
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioDTO getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(UsuarioDTO estudiante) {
        this.estudiante = estudiante;
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
