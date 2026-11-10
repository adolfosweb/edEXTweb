package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoDetalleDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;

@WebServlet(name = "MovilCursoServlet", urlPatterns = {"/movil/curso"})
public class MovilCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nombre del curso.");
            return;
        }

        // Opcional: si viene una edición, se muestra su detalle debajo del curso.
        String edicion = request.getParameter("edicion");
        if (edicion != null && edicion.isBlank()) {
            edicion = null;
        }

        try {
            CursoDetalleDTO curso = ClienteSOAP.obtenerServicio()
                    .consultarCursoDetalle(nombre, edicion, null);

            if (curso == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el curso solicitado o la edición no pertenece al curso.");
                return;
            }

            request.setAttribute("curso", curso);
            request.setAttribute("edicionSeleccionada", curso.getEdicionSeleccionada());
            request.getRequestDispatcher("/movil/componentes/curso.jsp").forward(request, response);

        } catch (WebServiceException e) {
            getServletContext().log("Error al consultar el curso mediante SOAP.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servidor central.");
        }
    }
}