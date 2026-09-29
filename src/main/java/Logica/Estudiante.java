/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Estudiante extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Inscripciones a cursos
    @OneToMany(mappedBy = "estudiante")
    private List<InscripcionCurso> inscripcionesCursos = new ArrayList<>();

    // Inscripciones a programas
    @OneToMany(mappedBy = "estudianteInscripto")
private List<InscripcionPrograma> inscripcionesProgramas = new ArrayList<>();

    public Estudiante() {
        super();
    }

    public Estudiante(String nickname, String correoElectronico,
            String nombre, String apellido, LocalDate fecNac) {

        super(nickname, correoElectronico, nombre, apellido, fecNac);
    }

    // =========================
    // INSCRIPCIONES A CURSOS
    // =========================

    public List<InscripcionCurso> getCursos() {
        return inscripcionesCursos;
    }

    public void agregarInscripcionCurso(InscripcionCurso ic) {

        if (ic != null && !inscripcionesCursos.contains(ic)) {

            inscripcionesCursos.add(ic);

            if (ic.getEstudiante() != this) {
                ic.setEstudiante(this);
            }
        }
    }

    public void eliminarInscripcionCurso(InscripcionCurso ic) {

        if (ic != null) {

            inscripcionesCursos.remove(ic);

            if (ic.getEstudiante() == this) {
                ic.setEstudiante(null);
            }
        }
    }

    // =========================
    // INSCRIPCIONES A PROGRAMAS
    // =========================

    public List<InscripcionPrograma> getProgramas() {
        return inscripcionesProgramas;
    }

    public void agregarInscripcionPrograma(InscripcionPrograma ip) {

        if (ip != null && !inscripcionesProgramas.contains(ip)) {

            inscripcionesProgramas.add(ip);

            if (ip.getEstudiante() != this) {
                ip.setEstudiante(this);
            }
        }
    }

    public void eliminarInscripcionPrograma(InscripcionPrograma ip) {

        if (ip != null) {

            inscripcionesProgramas.remove(ip);

            if (ip.getEstudiante() == this) {
                ip.setEstudiante(null);
            }
        }
    }
}