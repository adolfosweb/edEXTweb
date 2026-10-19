package Controladores;

import Datatypes.DtCurso;
import Datatypes.DtEdicionCurso;
import Datatypes.DtProgramaFormacion;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.Estudiante;
import Logica.InscripcionCurso;
import Logica.InscripcionPrograma;
import Logica.ProgramaFormacion;
import Logica.Curso;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SvltConsultarCurso", urlPatterns = {"/SvltConsultarCurso"})
public class SvltConsultarCurso extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nombre del curso.");
            return;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<Curso> cursos = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c "
                    + "LEFT JOIN FETCH c.instituto "
                    + "LEFT JOIN FETCH c.previas "
                    + "WHERE c.nombre = :nombre", Curso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();

            if (cursos.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el curso solicitado.");
                return;
            }

            Curso curso = cursos.get(0);
            DtCurso dtCurso = new DtCurso();
            dtCurso.setNombre(curso.getNombre());
            dtCurso.setDescripcion(curso.getDescripcion());
            dtCurso.setDuracion(curso.getDuracion());
            dtCurso.setCantHoras(curso.getCantHoras());
            dtCurso.setCantCreditos(curso.getCantCreditos());
            dtCurso.setFecRegistro(curso.getFecRegistro());
            dtCurso.setUrl(curso.getUrl());
            dtCurso.setInstituto(curso.getInstituto() == null ? null : curso.getInstituto().getNombre());

            List<String> nombresPrevias = new ArrayList<>();
            for (Curso previa : curso.getPrevias()) {
                nombresPrevias.add(previa.getNombre());
            }
            dtCurso.setPrevias(nombresPrevias);

            List<DtEdicionCurso> ediciones = new ArrayList<>();
            List<EdicionCurso> edicionesEntidad = em.createQuery(
                    "SELECT e FROM EdicionCurso e WHERE e.curso.nombre = :nombre ORDER BY e.nombre",
                    EdicionCurso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            for (EdicionCurso edicion : edicionesEntidad) {
                ediciones.add(convertirEdicion(edicion));
            }
            dtCurso.setEdiciones(ediciones);

            List<DtProgramaFormacion> programas = new ArrayList<>();
            List<ProgramaFormacion> programasEntidad = em.createQuery(
                    "SELECT DISTINCT p FROM ProgramaFormacion p JOIN p.cursos c "
                    + "WHERE c.nombre = :nombre ORDER BY p.nombre",
                    ProgramaFormacion.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            for (ProgramaFormacion programa : programasEntidad) {
                programas.add(convertirPrograma(programa));
            }
            dtCurso.setProgramas(programas);

            String nombreEdicion = request.getParameter("edicion");
            String nombrePrograma = request.getParameter("programa");
            if (nombreEdicion != null && !nombreEdicion.trim().isEmpty()) {
                List<EdicionCurso> seleccionadas = em.createQuery(
                        "SELECT e FROM EdicionCurso e WHERE e.nombre = :edicion "
                        + "AND e.curso.nombre = :curso", EdicionCurso.class)
                        .setParameter("edicion", nombreEdicion)
                        .setParameter("curso", nombre)
                        .getResultList();
                if (seleccionadas.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró la edición para este curso.");
                    return;
                }
                request.setAttribute("edicionSeleccionada", convertirEdicion(seleccionadas.get(0)));
            } else if (nombrePrograma != null && !nombrePrograma.trim().isEmpty()) {
                List<ProgramaFormacion> seleccionados = em.createQuery(
                        "SELECT DISTINCT p FROM ProgramaFormacion p JOIN p.cursos c "
                        + "WHERE p.nombre = :programa AND c.nombre = :curso",
                        ProgramaFormacion.class)
                        .setParameter("programa", nombrePrograma)
                        .setParameter("curso", nombre)
                        .getResultList();
                if (seleccionados.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el programa para este curso.");
                    return;
                }
                request.setAttribute("programaSeleccionado", convertirPrograma(seleccionados.get(0)));
            }

            request.setAttribute("curso", dtCurso);
            request.getRequestDispatcher("/componentes/cursos/consultarCurso.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }

    private DtEdicionCurso convertirEdicion(EdicionCurso edicion) {
        DtEdicionCurso dtEdicion = new DtEdicionCurso(
                edicion.getNombre(),
                edicion.getFechaInicio(),
                edicion.getFechaFin(),
                edicion.getCupo(),
                edicion.getFechaPublicacion(),
                edicion.getCurso() == null ? null : edicion.getCurso().getNombre());
        List<String> docentes = new ArrayList<>();
        for (Docente docente : edicion.getDocentes()) {
            docentes.add(docente.getNickname());
        }
        dtEdicion.setNombresDocentes(docentes);
        List<String> inscriptos = new ArrayList<>();
        for (InscripcionCurso inscripcion : edicion.getInscriptos()) {
            Estudiante estudiante = inscripcion.getEstudiante();
            if (estudiante != null) {
                inscriptos.add(estudiante.getNickname());
            }
        }
        dtEdicion.setNombresInscriptos(inscriptos);
        return dtEdicion;
    }

    private DtProgramaFormacion convertirPrograma(ProgramaFormacion programa) {
        DtProgramaFormacion dtPrograma = new DtProgramaFormacion(
                programa.getNombre(),
                programa.getDescripcion(),
                programa.getFechaInicio(),
                programa.getFechaFin(),
                programa.getFechaAlta());
        List<String> cursos = new ArrayList<>();
        for (Curso curso : programa.getCursos()) {
            cursos.add(curso.getNombre());
        }
        dtPrograma.setNombresCursos(cursos);
        List<String> inscriptos = new ArrayList<>();
        for (InscripcionPrograma inscripcion : programa.getInscriptos()) {
            Estudiante estudiante = inscripcion.getEstudiante();
            if (estudiante != null) {
                inscriptos.add(estudiante.getNickname());
            }
        }
        dtPrograma.setNombresInscriptos(inscriptos);
        return dtPrograma;
    }
}
