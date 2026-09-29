/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Docente extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Docente <-> Instituto = N:M
    @ManyToMany
    private List<Instituto> institutos = new ArrayList<>();

    // Docente <-> EdicionCurso = N:M
    @ManyToMany(mappedBy = "docentes")
    private List<EdicionCurso> edicionesParticipante = new ArrayList<>();

    public Docente() {
        super();
    }

    public Docente(String nickname,
            String correoElectronico,
            String nombre,
            String apellido,
            LocalDate fecNac) {

        super(nickname, correoElectronico, nombre, apellido, fecNac);
    }

    // ==========================================
    // INSTITUTOS
    // ==========================================

    public List<Instituto> getInstitutos() {
        return institutos;
    }

    public void agregarInstituto(Instituto i) {

        if (i != null && !institutos.contains(i)) {
            institutos.add(i);
        }
    }

    public void eliminarInstituto(Instituto i) {

        if (i != null) {
            institutos.remove(i);
        }
    }

    // ==========================================
    // CURSOS
    // ==========================================

    public List<Curso> getCursos() {

        List<Curso> cursos = new ArrayList<>();

        for (EdicionCurso edicion : edicionesParticipante) {

            if (edicion != null && edicion.getCurso() != null) {

                Curso curso = edicion.getCurso();

                if (!cursos.contains(curso)) {
                    cursos.add(curso);
                }
            }
        }

        return cursos;
    }

    // ==========================================
    // EDICIONES
    // ==========================================

    public List<EdicionCurso> getEdiciones() {
        return edicionesParticipante;
    }

    public void agregarEdicion(EdicionCurso ec) {

        if (ec != null && !edicionesParticipante.contains(ec)) {
            edicionesParticipante.add(ec);
        }
    }

    public void eliminarEdicion(EdicionCurso ec) {

        if (ec != null) {
            edicionesParticipante.remove(ec);
        }
    }
}