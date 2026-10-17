package com.mycompany.edextweb;

import Controladores.ControladorCurso;
import Controladores.ControladorPrograma;
import Logica.Curso;
import Logica.ProgramaFormacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet(name = "AgregarCursoAProgramaServlet",urlPatterns = {"/agregarCursoAPrograma"})
public class AgregarCursoAProgramaServlet extends HttpServlet {

    private ControladorCurso controladorCurso;
    private ControladorPrograma controladorPrograma;

    @Override
    public void init() throws ServletException {
        controladorCurso = new ControladorCurso();
        controladorPrograma = new ControladorPrograma();
    }

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {
        cargarFormulario(request);
        request.getRequestDispatcher("/componentes/agregarCursoAPrograma.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombrePrograma = request.getParameter("programa");
        String nombreCurso = request.getParameter("curso");

        if (nombrePrograma == null || nombrePrograma.isBlank()
                || nombreCurso == null || nombreCurso.isBlank()) {

            request.setAttribute("error","Debe seleccionar un programa y un curso.");

            cargarFormulario(request);
            request.getRequestDispatcher("/componentes/agregarCursoAPrograma.jsp").forward(request, response);
            return;
        }

        boolean agregado = controladorPrograma.agregarCurso(nombrePrograma,nombreCurso);

        if (agregado) {
            String programaCodificado = URLEncoder.encode(nombrePrograma,StandardCharsets.UTF_8);

            response.sendRedirect(request.getContextPath() + "/consultarPrograma?programa=" + programaCodificado);
            return;
        }

        request.setAttribute("error",
                "No se pudo agregar el curso. Verifique que el programa"
              + " y el curso existan y que el curso no esté ya asociado."
        );

        cargarFormulario(request);request.getRequestDispatcher("/componentes/agregarCursoAPrograma.jsp").forward(request, response);
    }

    private void cargarFormulario(HttpServletRequest request) {
        Map<String, ProgramaFormacion> programas = controladorPrograma.listarProgramas();

        Map<String, Curso> cursos = controladorCurso.listarCursos();

        request.setAttribute("programas", programas);
        request.setAttribute("cursos", cursos);
    }
}
