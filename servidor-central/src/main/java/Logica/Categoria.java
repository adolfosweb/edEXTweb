package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String nombre;
    
    @ManyToMany(mappedBy = "categorias")
    private List<Curso> cursos = new ArrayList<>();
    
    // Constructor requerido por JPA
    protected Categoria() {
    }
    
    public Categoria(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }
    
    public void agregarCurso(Curso c){
        if (c != null && !cursos.contains(c)) {
            cursos.add(c);
        }
    }
    
    public void eliminarCurso(Curso c){
        cursos.remove(c);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Categoria[ id=" + nombre + " ]";
    }
    
}
