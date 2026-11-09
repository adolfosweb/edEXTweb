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

@WebServlet(name = "SvltConsultarCurso", urlPatterns = {"/SvltConsultarCurso"})
public class ConsultarCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String nombre = request.getParameter("nombre");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Falta el nombre del curso.");
            return;
        }
        String nombreEdicion = request.getParameter("edicion");
        String nombrePrograma = request.getParameter("programa");
        try {
            CursoDetalleDTO curso = ClienteSOAP.obtenerServicio().consultarCursoDetalle(nombre, nombreEdicion, nombrePrograma);
            if (curso == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el curso solicitado o la selección no pertenece al curso.");
                return;
            }

            request.setAttribute("curso", curso);
            request.setAttribute("edicionSeleccionada", curso.getEdicionSeleccionada());
            request.setAttribute("programaSeleccionado", curso.getProgramaSeleccionado());

            request.getRequestDispatcher("/componentes/cursos/consultarCurso.jsp").forward(request, response);

        } catch (WebServiceException e) {
            getServletContext().log("Error al consultar el curso mediante SOAP.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servidor central.");
        }
    }
}