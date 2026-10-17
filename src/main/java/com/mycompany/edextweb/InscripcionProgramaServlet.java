package com.mycompany.edextweb;

import Controladores.ControladorPrograma;
import Logica.ProgramaFormacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "InscripcionProgramaServlet", urlPatterns = {"/inscripcionPrograma"})
public class InscripcionProgramaServlet extends HttpServlet {

    private ControladorPrograma controladorPrograma;

    @Override
    public void init() throws ServletException {
        controladorPrograma = new ControladorPrograma();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, ProgramaFormacion> programas = controladorPrograma.listarProgramas();

        request.setAttribute("programas", programas);
        request.getRequestDispatcher("/componentes/inscripcionPrograma.jsp").forward(request, response);
    }
}
