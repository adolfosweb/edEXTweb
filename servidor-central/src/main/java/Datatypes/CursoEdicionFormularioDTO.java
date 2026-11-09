package Datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CursoEdicionFormularioDTO", propOrder = {
    "nombreCurso",
    "nombreEdicion",
    "fechaInicio",
    "fechaFin",
    "cupo",
    "modoEdicion",
    "cursoEncontrado",
    "edicionEncontrada",
    "autorizado",
    "docentes",
    "docentesSeleccionados"
})
public class CursoEdicionFormularioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombreCurso;
    private String nombreEdicion;
    private String fechaInicio;
    private String fechaFin;
    private String cupo;

    private boolean modoEdicion;
    private boolean cursoEncontrado;
    private boolean edicionEncontrada;
    private boolean autorizado;

    @XmlElement(name = "docente")
    private List<String> docentes = new ArrayList<>();

    @XmlElement(name = "docenteSeleccionado")
    private List<String> docentesSeleccionados = new ArrayList<>();

    public CursoEdicionFormularioDTO() {
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public void setNombreEdicion(String nombreEdicion) {
        this.nombreEdicion = nombreEdicion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCupo() {
        return cupo;
    }

    public void setCupo(String cupo) {
        this.cupo = cupo;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    public boolean isCursoEncontrado() {
        return cursoEncontrado;
    }

    public void setCursoEncontrado(boolean cursoEncontrado) {
        this.cursoEncontrado = cursoEncontrado;
    }

    public boolean isEdicionEncontrada() {
        return edicionEncontrada;
    }

    public void setEdicionEncontrada(boolean edicionEncontrada) {
        this.edicionEncontrada = edicionEncontrada;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public List<String> getDocentes() {
        return docentes;
    }

    public List<String> getDocentesSeleccionados() {
        return docentesSeleccionados;
    }
}