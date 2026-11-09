package Datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerfilUsuarioDTO implements Serializable {

    private UsuarioDTO usuario;
    private List<InscripcionCursoDTO> inscripcionesCursos = new ArrayList<>();
    private List<InscripcionProgramaDTO> inscripcionesProgramas = new ArrayList<>();
    private List<EdicionPerfilDTO> edicionesDocente = new ArrayList<>();

    public PerfilUsuarioDTO() {
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public List<InscripcionCursoDTO> getInscripcionesCursos() {
        return inscripcionesCursos;
    }

    public void setInscripcionesCursos(List<InscripcionCursoDTO> inscripcionesCursos) {
        this.inscripcionesCursos = inscripcionesCursos;
    }

    public List<InscripcionProgramaDTO> getInscripcionesProgramas() {
        return inscripcionesProgramas;
    }

    public void setInscripcionesProgramas(List<InscripcionProgramaDTO> inscripcionesProgramas) {
        this.inscripcionesProgramas = inscripcionesProgramas;
    }

    public List<EdicionPerfilDTO> getEdicionesDocente() {
        return edicionesDocente;
    }

    public void setEdicionesDocente(List<EdicionPerfilDTO> edicionesDocente) {
        this.edicionesDocente = edicionesDocente;
    }
}