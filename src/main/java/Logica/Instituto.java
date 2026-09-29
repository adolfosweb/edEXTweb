/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instituto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String nombre;

    @ManyToMany(mappedBy = "institutos")
    private List<Docente> docentes = new ArrayList<>();

    @OneToMany(mappedBy = "instituto")
    private List<Curso> cursos;

    public Instituto() {
    }

    public Instituto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }

    public void agregarDocente(Docente d) {

        if (d != null) {
            docentes.add(d);
        }
    }

    public void eliminarDocente(Docente d) {

        if (d != null) {
            docentes.remove(d);
        }
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    // CAMBIO 2: Actualizar la lógica para mantener la coherencia 1 a N en memoria
    public void agregarCurso(Curso c) {

        if (c != null) {
            cursos.add(c);
        }
    }

    // CAMBIO 3: Desvincular el instituto del curso al eliminar
    public void eliminarCurso(Curso c) {

        if (c != null) {
            cursos.remove(c);
        }
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Instituto)) {
            return false;
        }

        Instituto other = (Instituto) obj;

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
