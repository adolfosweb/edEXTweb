package Datatypes;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EdicionDetalleDTO")
public class EdicionDetalleDTO {

    private String nombre;
    private String fechaInicio;
    private String fechaFin;
    private int cupo;
    private String fechaPublicacion;
    private String nombreCurso;
    private String imagen;

    @XmlElementWrapper(name = "docentes")
    @XmlElement(name = "docente")
    private List<String> nombresDocentes = new ArrayList<>();

    @XmlElementWrapper(name = "inscriptos")
    @XmlElement(name = "inscripto")
    private List<String> nombresInscriptos = new ArrayList<>();

    public EdicionDetalleDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public int getCupo() { return cupo; }
    public void setCupo(int cupo) { this.cupo = cupo; }
    
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public List<String> getNombresDocentes() { return nombresDocentes; }
    public void setNombresDocentes(List<String> nombresDocentes) {
        this.nombresDocentes = nombresDocentes;
    }

    public List<String> getNombresInscriptos() { return nombresInscriptos; }
    public void setNombresInscriptos(List<String> nombresInscriptos) {
        this.nombresInscriptos = nombresInscriptos;
    }
}