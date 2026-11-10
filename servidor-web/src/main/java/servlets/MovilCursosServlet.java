package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ListadoCursosDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.Collections;

@WebServlet(name = "MovilCursosServlet", urlPatterns = {"/movil/cursos"})
public class MovilCursosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            // Sin sesión todavía: se pide el listado sin usuario (igual que un visitante).
            ListadoCursosDTO listado = servicio.listarCursosParaWeb(null);
            request.setAttribute("cursos",
                    listado != null ? listado.getCursos() : Collections.emptyList());

            request.setAttribute("institutos", servicio.listarInstitutos());
        } catch (WebServiceException e) {
            request.setAttribute("cursos", Collections.emptyList());
            request.setAttribute("institutos", Collections.emptyList());
            request.setAttribute("mensajeError",
                    "No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        // Instituto elegido en el menú (puede venir vacío).
        request.setAttribute("institutoElegido", request.getParameter("instituto"));

        request.getRequestDispatcher("/movil/componentes/cursos.jsp").forward(request, response);
    }
}