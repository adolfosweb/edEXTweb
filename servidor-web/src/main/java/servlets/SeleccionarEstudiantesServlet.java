package com.mycompany.edextweb;

import Controladores.ControladorCurso;
import Controladores.ControladorInstituto;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.InscripcionCurso;
import Logica.Instituto;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import Logica.EstadoInscripcion;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "SeleccionarEstudiantesServlet", urlPatterns = {"/seleccionarEstudiantes"})
public class SeleccionarEstudiantesServlet extends HttpServlet {

    private ControladorCurso controladorCurso;

    @Override
    public void init() throws ServletException {
        controladorCurso = new ControladorCurso();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof Docente)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los docentes pueden gestionar inscripciones.");
            return;
        }

        Docente docente = (Docente) session.getAttribute("usuario");
        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");

        try {
            // Cargar únicamente los institutos asociados al docente.
            EntityManager em = Controladores.Conexion.getInstancia().getEntityManager();

            List<Instituto> institutos;
            try {
                institutos = em.createQuery(
                        "SELECT i FROM Docente d JOIN d.institutos i "
                        + "WHERE d.nickname = :nickname ORDER BY i.nombre",
                        Instituto.class).setParameter("nickname", docente.getNickname()).getResultList();
            } finally {
                em.close();
            }

            request.setAttribute("institutos", institutos);

            // Si se seleccionó un instituto, cargar sus cursos.
            if (institutoNombre != null && !institutoNombre.isBlank()) {

                EntityManager emCursos = Controladores.Conexion.getInstancia().getEntityManager();

                List<Logica.Curso> cursos;
                try {
                    cursos = emCursos.createQuery(
                            "SELECT c FROM Curso c "
                            + "JOIN c.instituto i "
                            + "JOIN i.docentes d "
                            + "WHERE i.nombre = :instituto "
                            + "AND d.nickname = :nickname "
                            + "ORDER BY c.nombre",
                            Logica.Curso.class)
                            .setParameter("instituto", institutoNombre)
                            .setParameter("nickname", docente.getNickname())
                            .getResultList();
                } finally {
                    emCursos.close();
                }

                request.setAttribute("cursos", cursos);
                request.setAttribute("institutoSeleccionado", institutoNombre);

                // Si se seleccionó un curso, cargar sus ediciones.
                if (cursoNombre != null && !cursoNombre.isBlank()) {

                    EntityManager emEdiciones = Controladores.Conexion.getInstancia().getEntityManager();

                    List<EdicionCurso> ediciones;
                    try {
                        ediciones = emEdiciones.createQuery(
                                "SELECT e FROM EdicionCurso e "
                                + "JOIN FETCH e.curso c "
                                + "JOIN c.instituto i "
                                + "JOIN e.docentes d "
                                + "WHERE c.nombre = :curso "
                                + "AND i.nombre = :instituto "
                                + "AND d.nickname = :nickname "
                                + "ORDER BY e.fechaInicio",
                                EdicionCurso.class)
                                .setParameter("curso", cursoNombre)
                                .setParameter("instituto", institutoNombre)
                                .setParameter("nickname", docente.getNickname())
                                .getResultList();
                    } finally {
                        emEdiciones.close();
                    }

                    request.setAttribute("ediciones", ediciones);
                    request.setAttribute("cursoSeleccionado", cursoNombre);

                    // Mostrar inscripciones si se seleccionó una edición.
                    if (edicionNombre != null && !edicionNombre.isBlank()) {

                        boolean edicionPermitida = ediciones.stream().anyMatch(e -> e.getNombre().equals(edicionNombre));

                        if (!edicionPermitida) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,"No puedes gestionar esa edición.");
                            return;
                        }

                        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEdicion(edicionNombre);

                        request.setAttribute("inscripciones", inscripciones);
                        request.setAttribute("edicionSeleccionada", edicionNombre);
                    }
                }
            }

        } catch (Exception e) {
            throw new ServletException("No se pudieron cargar las inscripciones.", e);
        }
        Object mensaje = session.getAttribute("mensajeSeleccion");

        if (mensaje != null) {
            request.setAttribute("mensajeSeleccion", mensaje);
            session.removeAttribute("mensajeSeleccion");
        }
        request.getRequestDispatcher("/componentes/seleccionarEstudiantes.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof Docente)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los docentes pueden gestionar inscripciones.");
            return;
        }

        Docente docente = (Docente) session.getAttribute("usuario");

        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");
        String idTexto = request.getParameter("idInscripcion");
        String estadoTexto = request.getParameter("estado");

        // Solo se permite aceptar o rechazar.
        EstadoInscripcion nuevoEstado;

        try {
            if ("ACEPTADA".equals(estadoTexto)) {
                nuevoEstado = EstadoInscripcion.ACEPTADA;
            } else if ("RECHAZADA".equals(estadoTexto)) {
                nuevoEstado = EstadoInscripcion.RECHAZADA;
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"El estado seleccionado no es válido.");
                return;
            }

            if (institutoNombre == null || institutoNombre.isBlank()
                    || cursoNombre == null || cursoNombre.isBlank()
                    || edicionNombre == null || edicionNombre.isBlank()
                    || idTexto == null || idTexto.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Faltan datos para gestionar la inscripción.");
                return;
            }

            Long idInscripcion = Long.valueOf(idTexto);

            EntityManager em = Controladores.Conexion.getInstancia().getEntityManager();

            boolean puedeGestionar;
            boolean inscripcionPertenece;

            try {
                // Verificar que el docente participa en esa edición
                // y que corresponde al curso e instituto seleccionados.
                Long cantidadEdiciones = em.createQuery(
                        "SELECT COUNT(e) FROM EdicionCurso e "
                        + "JOIN e.docentes d "
                        + "WHERE e.nombre = :edicion "
                        + "AND d.nickname = :nickname "
                        + "AND e.curso.nombre = :curso "
                        + "AND e.curso.instituto.nombre = :instituto",
                        Long.class)
                        .setParameter("edicion", edicionNombre)
                        .setParameter("nickname", docente.getNickname())
                        .setParameter("curso", cursoNombre)
                        .setParameter("instituto", institutoNombre)
                        .getSingleResult();

                puedeGestionar = cantidadEdiciones > 0;

                // Comprobar que la inscripción pertenece a esa edición.
                Long cantidadInscripciones = em.createQuery(
                        "SELECT COUNT(i) FROM InscripcionCurso i "
                        + "WHERE i.id = :id "
                        + "AND i.edicionCurso.nombre = :edicion",
                        Long.class)
                        .setParameter("id", idInscripcion)
                        .setParameter("edicion", edicionNombre)
                        .getSingleResult();

                inscripcionPertenece = cantidadInscripciones > 0;

            } finally {
                em.close();
            }

            if (!puedeGestionar) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"No puedes gestionar esa edición.");
                return;
            }

            if (!inscripcionPertenece) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"La inscripción no pertenece a la edición seleccionada.");
                return;
            }

            boolean actualizado = controladorCurso.cambiarEstadoInscripcion(idInscripcion, nuevoEstado);

            if (!actualizado) {
                throw new ServletException("No se pudo actualizar el estado de la inscripción.");
            }

            session.setAttribute("mensajeSeleccion","La inscripción fue "
                    + (nuevoEstado == EstadoInscripcion.ACEPTADA ? "aceptada correctamente." : "rechazada correctamente."));

            // PRG: redirigir para evitar repetir el POST al recargar.
            String url = request.getContextPath()
                    + "/seleccionarEstudiantes"
                    + "?instituto=" + URLEncoder.encode(institutoNombre, StandardCharsets.UTF_8)
                    + "&curso=" + URLEncoder.encode(cursoNombre, StandardCharsets.UTF_8)
                    + "&edicion=" + URLEncoder.encode(edicionNombre, StandardCharsets.UTF_8);

            response.sendRedirect(url);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"El identificador de inscripción no es válido.");
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("No se pudo gestionar la inscripción.", e);
        }
    }

}
