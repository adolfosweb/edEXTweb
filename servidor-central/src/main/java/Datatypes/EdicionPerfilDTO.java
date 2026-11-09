package Datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EdicionPerfilDTO implements Serializable {

    private String nombreEdicion;
    private String nombreCurso;
    private List<UsuarioDTO> estudiantesAceptados = new ArrayList<>();

    public EdicionPerfilDTO() {
    }

    public EdicionPerfilDTO(String nombreEdicion, String nombreCurso) {
        this.nombreEdicion = nombreEdicion;
        this.nombreCurso = nombreCurso;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public void setNombreEdicion(String nombreEdicion) {
        this.nombreEdicion = nombreEdicion;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public List<UsuarioDTO> getEstudiantesAceptados() {
        return estudiantesAceptados;
    }

    public void setEstudiantesAceptados(List<UsuarioDTO> estudiantesAceptados) {
        this.estudiantesAceptados = estudiantesAceptados;
    }
}