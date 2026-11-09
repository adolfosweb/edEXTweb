package Datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CursoInscripcionDTO")
public class CursoInscripcionDTO {

    private String nombre;
    private String descripcion;
    private String duracion;
    private List<EdicionCursoDTO> ediciones = new ArrayList<>();

    public CursoInscripcionDTO() {
    }

    public CursoInscripcionDTO(String nombre, String descripcion, String duracion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
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

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public List<EdicionCursoDTO> getEdiciones() {
        return ediciones;
    }

    public void setEdiciones(List<EdicionCursoDTO> ediciones) {
        this.ediciones = ediciones;
    }
}
