/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Datatypes;
/**
 *
 * @author adolfo
 */


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DTCurso implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String descripcion;
    private String duracion;
    private int cantHoras;
    private int cantCreditos;
    private Date fecRegistro;
    private String url;
    private String instituto;
    private String imagen;
    private List<String> previas;
    private List<String> categorias;

    // CONSTRUCTOR DE 11 PARÁMETROS
    public DTCurso(String nombre, String descripcion, String duracion, int cantHoras, 
                   int cantCreditos, Date fecRegistro, String url, String instituto, 
                   String imagen, List<String> previas, List<String> categorias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCreditos = cantCreditos;
        this.fecRegistro = fecRegistro;
        this.url = url;
        this.instituto = instituto;
        this.imagen = imagen;
        this.previas = previas;
        this.categorias = categorias;
    }

    // Getters...
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDuracion() { return duracion; }
    public int getCantHoras() { return cantHoras; }
    public int getCantCreditos() { return cantCreditos; }
    public Date getFecRegistro() { return fecRegistro; }
    public String getUrl() { return url; }
    public String getInstituto() { return instituto; }
    public String getImagen() { return imagen; }
    public List<String> getPrevias() { return previas; }
    public List<String> getCategorias() { return categorias; }
}