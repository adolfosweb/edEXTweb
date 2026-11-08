package com.mycompany.edextweb;

import Logica.Estudiante;
import Logica.InscripcionCurso;
import Logica.Usuario;
import Controladores.ControladorCurso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ResultadosInscripcionesServlet", urlPatterns = {"/resultadosInscripciones"})
public class ResultadosInscripcionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof Usuario)) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (!(usuario instanceof Estudiante)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los estudiantes pueden consultar sus resultados.");
            return;
        }

        Estudiante estudiante = (Estudiante) usuario;

        ControladorCurso controladorCurso = new ControladorCurso();
        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEstudiante(estudiante.getNickname());

        request.setAttribute("inscripciones", inscripciones);
        request.getRequestDispatcher("/componentes/resultadoInscripciones.jsp").forward(request, response);
    }
}
