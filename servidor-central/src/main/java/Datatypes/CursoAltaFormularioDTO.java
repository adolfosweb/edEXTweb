package Datatypes;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CursoAltaFormularioDTO")
public class CursoAltaFormularioDTO {

    private String nombre;
    private String descripcion;
    private String duracion;
    private int cantHoras;
    private int cantCreditos;
    private String url;
    private String instituto;
    private String fechaRegistro;

    private boolean modoEdicion;
    private boolean cursoEncontrado;
    private boolean autorizado;

    @XmlElementWrapper(name = "institutos")
    @XmlElement(name = "instituto")
    private List<String> institutos = new ArrayList<>();

    @XmlElementWrapper(name = "cursosExistentes")
    @XmlElement(name = "curso")
    private List<String> cursosExistentes = new ArrayList<>();

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    private List<String> categorias = new ArrayList<>();

    @XmlElementWrapper(name = "previasSeleccionadas")
    @XmlElement(name = "previa")
    private List<String> previasSeleccionadas = new ArrayList<>();

    @XmlElementWrapper(name = "categoriasSeleccionadas")
    @XmlElement(name = "categoriaSeleccionada")
    private List<String> categoriasSeleccionadas = new ArrayList<>();

    public CursoAltaFormularioDTO() {}

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

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getInstituto() { return instituto; }
    public void setInstituto(String instituto) { this.instituto = instituto; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean isModoEdicion() { return modoEdicion; }
    public void setModoEdicion(boolean modoEdicion) { this.modoEdicion = modoEdicion; }

    public boolean isCursoEncontrado() { return cursoEncontrado; }
    public void setCursoEncontrado(boolean cursoEncontrado) { this.cursoEncontrado = cursoEncontrado; }

    public boolean isAutorizado() { return autorizado; }
    public void setAutorizado(boolean autorizado) { this.autorizado = autorizado; }

    public List<String> getInstitutos() { return institutos; }
    public void setInstitutos(List<String> institutos) { this.institutos = institutos; }

    public List<String> getCursosExistentes() { return cursosExistentes; }
    public void setCursosExistentes(List<String> cursosExistentes) {
        this.cursosExistentes = cursosExistentes;
    }

    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }

    public List<String> getPreviasSeleccionadas() { return previasSeleccionadas; }
    public void setPreviasSeleccionadas(List<String> previasSeleccionadas) {
        this.previasSeleccionadas = previasSeleccionadas;
    }

    public List<String> getCategoriasSeleccionadas() { return categoriasSeleccionadas; }
    public void setCategoriasSeleccionadas(List<String> categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
    }
}