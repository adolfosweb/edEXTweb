package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoDetalleDTO;
import com.mycompany.edextweb.soap.CursoListadoDTO;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ListadoCursosDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Consulta de Edición de curso (móvil):
 * instituto -> cursos del instituto -> ediciones del curso -> datos de la edición.
 * Cada paso se indica con los parámetros ?instituto=...&curso=...&edicion=...
 */
@WebServlet(name = "MovilEdicionesServlet", urlPatterns = {"/movil/ediciones"})
public class MovilEdicionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String instituto = parametro(request, "instituto");
        String curso = parametro(request, "curso");
        String edicion = parametro(request, "edicion");

        TreeSet<String> institutos = new TreeSet<>();
        List<CursoListadoDTO> cursos = new ArrayList<>();

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            ListadoCursosDTO listado = servicio.listarCursosParaWeb(null);
            List<CursoListadoDTO> todos = listado != null ? listado.getCursos() : Collections.emptyList();
            for (CursoListadoDTO c : todos) {
                if (c.getInstituto() != null) {
                    institutos.add(c.getInstituto());
                }
                if (instituto != null && instituto.equals(c.getInstituto())) {
                    cursos.add(c);
                }
            }

            if (instituto != null && curso != null) {
                CursoDetalleDTO detalle = servicio.consultarCursoDetalle(curso, edicion, null);
                request.setAttribute("curso", detalle);
                if (detalle != null) {
                    request.setAttribute("edicionSeleccionada", detalle.getEdicionSeleccionada());
                }
            }
        } catch (WebServiceException e) {
            getServletContext().log("Error al consultar ediciones mediante SOAP.", e);
            request.setAttribute("mensajeError",
                    "No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursos", cursos);
        request.setAttribute("institutoElegido", instituto);
        request.setAttribute("cursoElegido", curso);

        request.getRequestDispatcher("/movil/componentes/ediciones.jsp").forward(request, response);
    }

    private String parametro(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return (valor == null || valor.isBlank()) ? null : valor;
    }
}