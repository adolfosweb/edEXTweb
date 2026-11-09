package Datatypes;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CursoDetalleDTO")
public class CursoDetalleDTO {

    private String nombre;
    private String descripcion;
    private String duracion;
    private int cantHoras;
    private int cantCreditos;
    private String fechaRegistro;
    private String url;
    private String instituto;

    @XmlElementWrapper(name = "previas")
    @XmlElement(name = "previa")
    private List<String> previas = new ArrayList<>();

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    private List<String> categorias = new ArrayList<>();

    @XmlElementWrapper(name = "ediciones")
    @XmlElement(name = "edicion")
    private List<EdicionDetalleDTO> ediciones = new ArrayList<>();

    @XmlElementWrapper(name = "programas")
    @XmlElement(name = "programa")
    private List<ProgramaDetalleDTO> programas = new ArrayList<>();

    private EdicionDetalleDTO edicionSeleccionada;
    private ProgramaDetalleDTO programaSeleccionado;

    public CursoDetalleDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public int getCantHoras() { return cantHoras; }
    public void setCantHoras(int cantHoras) { this.cantHoras = cantHoras; }

    public int getCantCreditos() { return cantCreditos; }
    public void setCantCreditos(int cantCreditos) { this.cantCreditos = cantCreditos; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getInstituto() { return instituto; }
    public void setInstituto(String instituto) { this.instituto = instituto; }

    public List<String> getPrevias() { return previas; }
    public void setPrevias(List<String> previas) { this.previas = previas; }

    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }

    public List<EdicionDetalleDTO> getEdiciones() { return ediciones; }
    public void setEdiciones(List<EdicionDetalleDTO> ediciones) { this.ediciones = ediciones; }

    public List<ProgramaDetalleDTO> getProgramas() { return programas; }
    public void setProgramas(List<ProgramaDetalleDTO> programas) { this.programas = programas; }

    public EdicionDetalleDTO getEdicionSeleccionada() { return edicionSeleccionada; }
    public void setEdicionSeleccionada(EdicionDetalleDTO edicionSeleccionada) {
        this.edicionSeleccionada = edicionSeleccionada;
    }

    public ProgramaDetalleDTO getProgramaSeleccionado() { return programaSeleccionado; }
    public void setProgramaSeleccionado(ProgramaDetalleDTO programaSeleccionado) {
        this.programaSeleccionado = programaSeleccionado;
    }
}