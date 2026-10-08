/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

/**
 *
 * @author adolfo
 */


import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import Datatypes.DTCurso;

@WebServlet("/ListarCursos")
public class SvltListarCursos extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            IControladorCurso icon = new ControladorCurso();
            List<DTCurso> listaCursos = icon.obtenerListaCursosDT();

            request.setAttribute("listaCursos", listaCursos);
            request.setAttribute("contenido", "/componentes/vistas/listarCursos.jsp");

        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al cargar los cursos: " + e.getMessage());
            request.setAttribute("contenido", "/componentes/vistas/home.jsp");
        }

        request.getRequestDispatcher("/componentes/principal.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
