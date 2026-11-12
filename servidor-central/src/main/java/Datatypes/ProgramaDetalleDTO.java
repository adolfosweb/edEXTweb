package Datatypes;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProgramaDetalleDTO")
public class ProgramaDetalleDTO {

    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String fechaAlta;
    private String imagen;

    @XmlElementWrapper(name = "cursos")
    @XmlElement(name = "curso")
    private List<String> nombresCursos = new ArrayList<>();

    @XmlElementWrapper(name = "inscriptos")
    @XmlElement(name = "inscripto")
    private List<String> nombresInscriptos = new ArrayList<>();

    public ProgramaDetalleDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(String fechaAlta) { this.fechaAlta = fechaAlta; }
    
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public List<String> getNombresCursos() { return nombresCursos; }
    public void setNombresCursos(List<String> nombresCursos) {
        this.nombresCursos = nombresCursos;
    }

    public List<String> getNombresInscriptos() { return nombresInscriptos; }
    public void setNombresInscriptos(List<String> nombresInscriptos) {
        this.nombresInscriptos = nombresInscriptos;
    }
}