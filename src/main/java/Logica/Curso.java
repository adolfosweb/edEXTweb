///*
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Logica;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    private String duracion;
    private int cantHoras;
    private int cantCreditos;

    @Temporal(TemporalType.DATE)
    private Date fecRegistro;

    private String url;

    // Un curso se brinda en un solo instituto
    @ManyToOne
    private Instituto instituto;

    // Un curso puede tener muchas ediciones
    @OneToMany(mappedBy = "curso")
    private List<EdicionCurso> ediciones = new ArrayList<>();

    // Un curso puede pertenecer a varios programas
    @ManyToMany(mappedBy = "cursos")
    private List<ProgramaFormacion> programas = new ArrayList<>();

    // Relación auto-referenciada: Cursos que son correlativas/previas de este curso
    @ManyToMany
    @JoinTable(
        name = "curso_previas",
        joinColumns = @JoinColumn(name = "curso_nombre"),
        inverseJoinColumns = @JoinColumn(name = "previa_nombre")
    )
    private List<Curso> previas = new ArrayList<>();

    // =========================================================================
    // CONSTRUCTORES
    // =========================================================================

    public Curso() {
    }

    public Curso(String nombre, String descripcion, String duracion,
                 int cantHoras, int cantCreditos,
                 Date fecRegistro, String url) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCreditos = cantCreditos;
        this.fecRegistro = fecRegistro;
        this.url = url; 
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

    public Instituto getInstituto() {
        return instituto;
    }

    public List<EdicionCurso> getEdiciones() {
        return ediciones;
    }

    public List<ProgramaFormacion> getProgramas() {
        return programas;
    }

    public List<Curso> getPrevias() {
        return previas;
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

    public void setInstituto(Instituto instituto) {
        this.instituto = instituto;
    }

    public void setEdiciones(List<EdicionCurso> ediciones) {
        this.ediciones = ediciones;
    }

    public void setProgramas(List<ProgramaFormacion> programas) {
        this.programas = programas;
    }

    public void setPrevias(List<Curso> previas) {
        this.previas = previas;
    }

    // =========================================================================
    // MÉTODOS DE NEGOCIO / HELPER
    // =========================================================================

    public void asociarInstituto(Instituto i) {
        this.instituto = i;
        if (i != null && !i.getCursos().contains(this)) {
            i.getCursos().add(this);
        }
    }

    public void agregarEdicion(EdicionCurso edicion) {
        if (edicion != null && !ediciones.contains(edicion)) {
            ediciones.add(edicion);
        }
    }

    public void eliminarEdicion(EdicionCurso edicion) {
        if (edicion != null) {
            ediciones.remove(edicion);
        }
    }

    public void agregarPrograma(ProgramaFormacion programa) {
        if (programa != null && !programas.contains(programa)) {
            programas.add(programa);
        }
    }

    public void eliminarPrograma(ProgramaFormacion programa) {
        if (programa != null) {
            programas.remove(programa);
        }
    }

    public void agregarPrevia(Curso c) {
        if (c != null && !previas.contains(c)) {
            previas.add(c);
        }
    }

    public void eliminarPrevia(Curso c) {
        if (c != null) {
            previas.remove(c);
        }
    }

    // =========================================================================
    // OVERRIDES (hashCode, equals, toString)
    // =========================================================================

    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Curso)) {
            return false;
        }

        Curso other = (Curso) object;

        if (nombre == null && other.nombre != null) {
            return false;
        }

        if (nombre != null && !nombre.equals(other.nombre)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}