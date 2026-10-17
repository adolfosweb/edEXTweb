package com.mycompany.edextweb;

import Controladores.ControladorInstituto;
import Logica.Instituto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "InscripcionEdicionServlet", urlPatterns = {"/inscripcionEdicion"})
public class InscripcionEdicionServlet extends HttpServlet {
private ControladorInstituto controladorInstituto;

    @Override
    public void init() throws ServletException {
        controladorInstituto = new ControladorInstituto();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Instituto> institutos = controladorInstituto.listarInstitutos();
        request.setAttribute("institutos", institutos);

        // Obtener el instituto seleccionado, si existe.
        String nombreInstituto = request.getParameter("instituto");

        if (nombreInstituto != null && !nombreInstituto.trim().isEmpty()) {
            Instituto instituto = controladorInstituto.buscarInstituto(nombreInstituto);
            if (instituto != null) {
                request.setAttribute("institutoSeleccionado", instituto);
            } else {
                request.setAttribute("error","No se encontró el instituto seleccionado.");
            }
        }
        request.getRequestDispatcher("/componentes/inscripcionEdicion.jsp").forward(request, response);
    }
}
