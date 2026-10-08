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

// Importamos tus Data Types del paquete Datatypes
import Datatypes.DTCurso;
import Datatypes.DTEdicionCurso;
import Datatypes.DTProgramaFormacion;

@WebServlet("/ConsultaCurso")
public class SvltConsultaCurso extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nombreCurso = request.getParameter("nombre");

        if (nombreCurso != null && !nombreCurso.trim().isEmpty()) {
            try {
                // Instanciamos el controlador central
                IControladorCurso icon = new ControladorCurso();

                // Invocamos los métodos de conversión a Data Types
                DTCurso curso = icon.obtenerInformacionCurso(nombreCurso);
                List<DTEdicionCurso> ediciones = icon.obtenerEdicionesCurso(nombreCurso);
                List<DTProgramaFormacion> programas = icon.obtenerProgramasCurso(nombreCurso);

                // Seteamos los atributos para la vista
                request.setAttribute("cursoSeleccionado", curso);
                request.setAttribute("listaEdiciones", ediciones);
                request.setAttribute("listaProgramas", programas);
                
                // Definimos el JSP central a renderizar dentro de la plantilla
                request.setAttribute("contenido", "/componentes/vistas/consultaCurso.jsp");

            } catch (Exception e) {
                request.setAttribute("mensajeError", "Error al consultar la información del curso: " + e.getMessage());
                request.setAttribute("contenido", "/componentes/vistas/home.jsp");
            }
        } else {
            // Si no se especificó un parámetro, enviamos al inicio
            request.setAttribute("contenido", "/componentes/vistas/home.jsp");
        }

        // Redirigimos hacia la plantilla contenedora general principal.jsp
        request.getRequestDispatcher("/componentes/principal.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
}