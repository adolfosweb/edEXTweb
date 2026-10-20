package com.mycompany.edextweb;

import Controladores.ControladorCurso;
import Controladores.ControladorInstituto;
import Logica.EdicionCurso;
import Logica.Estudiante;
import Logica.InscripcionCurso;
import Logica.Instituto;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InscripcionEdicionServlet", urlPatterns = {"/inscripcionEdicion"})
public class InscripcionEdicionServlet extends HttpServlet {

    private ControladorInstituto controladorInstituto;
    private ControladorCurso controladorCurso;

    @Override
    public void init() throws ServletException {
        controladorInstituto = new ControladorInstituto();
        controladorCurso = new ControladorCurso();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarPagina(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String contexto = request.getContextPath();
        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof Usuario)) {
            response.sendRedirect(contexto + "/componentes/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (!(usuario instanceof Estudiante)) {
            request.setAttribute("error","Solo los estudiantes pueden inscribirse a ediciones de cursos.");
            mostrarPagina(request, response);
            return;
        }

        String nombreInstituto = request.getParameter("instituto");
        String nombreEdicion = request.getParameter("edicion");

        if (nombreInstituto == null || nombreInstituto.isBlank()
                || nombreEdicion == null || nombreEdicion.isBlank()) {
            request.setAttribute("error","No se especificó el instituto o la edición.");
            mostrarPagina(request, response);
            return;
        }

        Instituto instituto = controladorInstituto.buscarInstituto(nombreInstituto);

        EdicionCurso edicion = controladorCurso.buscarEdicion(nombreEdicion);

        if (instituto == null || edicion == null
                || edicion.getCurso() == null
                || edicion.getCurso().getInstituto() == null
                || !nombreInstituto.equals(edicion.getCurso().getInstituto().getNombre())) {
            request.setAttribute("error","No se encontró la edición seleccionada en ese instituto.");
            mostrarPagina(request, response);
            return;
        }

        Estudiante estudiante = (Estudiante) usuario;

        InscripcionCurso existente = controladorCurso.buscarInscripcion(estudiante.getNickname(),edicion.getNombre());

        if (existente != null) {
            request.setAttribute("error","Ya estás inscrito en esta edición.");
            mostrarPagina(request, response);
            return;
        }

        InscripcionCurso inscripcion = new InscripcionCurso(estudiante,LocalDate.now(),edicion);

        boolean resultado = controladorCurso.inscribirEstudiante(inscripcion);

        if (resultado) {
            request.setAttribute("mensaje","Te inscribiste correctamente a la edición "+ edicion.getNombre() + ".");
        } else {
            request.setAttribute("error",
                "No se pudo completar la inscripción. "
              + "Es posible que no haya cupos disponibles "
              + "o que haya ocurrido un error."
            );
        }

        mostrarPagina(request, response);
    }

    private void mostrarPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Instituto> institutos = controladorInstituto.listarInstitutos();

        request.setAttribute("institutos", institutos);

        String nombreInstituto = request.getParameter("instituto");

        if (nombreInstituto != null && !nombreInstituto.isBlank()) {
            Instituto instituto = controladorInstituto.buscarInstituto(nombreInstituto);

            if (instituto != null) {
                request.setAttribute("institutoSeleccionado", instituto);

                Map<String, EdicionCurso> todasLasEdiciones = controladorCurso.listarEdiciones();

                Map<String, List<EdicionCurso>> edicionesPorCurso = new HashMap<>();

                for (EdicionCurso edicion : todasLasEdiciones.values()) {
                    if (edicion.getCurso() != null
                            && edicion.getCurso().getInstituto() != null
                            && nombreInstituto.equals(edicion.getCurso().getInstituto().getNombre())) {

                        String nombreCurso = edicion.getCurso().getNombre();

                        edicionesPorCurso.computeIfAbsent(nombreCurso,k -> new java.util.ArrayList<>()).add(edicion);
                    }
                }

                request.setAttribute("edicionesPorCurso",edicionesPorCurso);
            } else {
                request.setAttribute("error","No se encontró el instituto seleccionado.");
            }
        }

        request.getRequestDispatcher("/componentes/inscripcionEdicion.jsp").forward(request, response);
    }
}
