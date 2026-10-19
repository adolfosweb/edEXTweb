package Datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DtCurso implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String descripcion;
    private String duracion;
    private int cantHoras;
    private int cantCreditos;
    private Date fecRegistro;
    private String url;
    private String urlFoto;
    private String instituto;

    // Colecciones de DTOs / Nombres para la vista web
    private List<DtEdicionCurso> ediciones;
    private List<DtProgramaFormacion> programas;
    private List<String> previas;
    private List<String> categorias;

    // =========================================================================
    // CONSTRUCTORES
    // =========================================================================

    public DtCurso() {
        this.ediciones = new ArrayList<>();
        this.programas = new ArrayList<>();
        this.previas = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    // Constructor completo para la Consulta de Curso en la Web
    public DtCurso(String nombre, String descripcion, String duracion, int cantHoras, 
                   int cantCreditos, Date fecRegistro, String url, String urlFoto, 
                   String instituto, List<DtEdicionCurso> ediciones, 
                   List<DtProgramaFormacion> programas, List<String> previas, 
                   List<String> categorias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCreditos = cantCreditos;
        this.fecRegistro = fecRegistro;
        this.url = url;
        this.urlFoto = urlFoto;
        this.instituto = instituto;
        this.ediciones = ediciones != null ? ediciones : new ArrayList<>();
        this.programas = programas != null ? programas : new ArrayList<>();
        this.previas = previas != null ? previas : new ArrayList<>();
        this.categorias = categorias != null ? categorias : new ArrayList<>();
    }

    // Constructor simplificado (para listados generales/cards)
    public DtCurso(String nombre, String descripcion, String duracion, String urlFoto, String instituto) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.urlFoto = urlFoto;
        this.instituto = instituto;
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public int getCantCreditos() {
        return cantCreditos;
    }

    public Date getFecRegistro() {
        return fecRegistro;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public String getInstituto() {
        return instituto;
    }

    public List<DtEdicionCurso> getEdiciones() {
        return ediciones;
    }

    public List<DtProgramaFormacion> getProgramas() {
        return programas;
    }

    public List<String> getPrevias() {
        return previas;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    // =========================================================================
    // SETTERS
    // =========================================================================

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public void setCantCreditos(int cantCreditos) {
        this.cantCreditos = cantCreditos;
    }

    public void setFecRegistro(Date fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public void setInstituto(String instituto) {
        this.instituto = instituto;
    }

    public void setEdiciones(List<DtEdicionCurso> ediciones) {
        this.ediciones = ediciones;
    }

    public void setProgramas(List<DtProgramaFormacion> programas) {
        this.programas = programas;
    }

    public void setPrevias(List<String> previas) {
        this.previas = previas;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    @Override
    public String toString() {
        return nombre;
    }
}