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

@WebServlet(name = "ConsultarProgramaServlet", urlPatterns = {"/consultarPrograma"})
public class ConsultarProgramaServlet extends HttpServlet {

    private ControladorPrograma controladorPrograma;

    @Override
    public void init() throws ServletException {
        controladorPrograma = new ControladorPrograma();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, ProgramaFormacion> programas = controladorPrograma.listarProgramas();

        String nombreSeleccionado = request.getParameter("programa");

        ProgramaFormacion programaSeleccionado = null;

        if (nombreSeleccionado != null && !nombreSeleccionado.isBlank()) {
            programaSeleccionado = controladorPrograma.buscarPrograma(nombreSeleccionado);
        } else if (programas != null && !programas.isEmpty()) {
            // Si todavía no se eligió ninguno, mostrar el primero
            programaSeleccionado = programas.values().iterator().next();
        }
        // Enviar los datos al JSP
        request.setAttribute("programas", programas);
        request.setAttribute("programaSeleccionado", programaSeleccionado);

        // Mostrar la página
        request.getRequestDispatcher("/componentes/consultarPrograma.jsp").forward(request, response);
    }
}
