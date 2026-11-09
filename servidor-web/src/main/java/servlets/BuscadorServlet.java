package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ResultadoBusquedaDTO;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "BuscadorServlet", urlPatterns = {"/buscador"})
public class BuscadorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String query = request.getParameter("q");
        String filtro = request.getParameter("filtro");
        String orden = request.getParameter("orden");

        query = query == null ? "" : query.trim();
        filtro = filtro == null ? "todos" : filtro;
        orden = orden == null ? "nombre_asc" : orden;

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            List<ResultadoBusquedaDTO> resultados = servicio.buscar(query, filtro, orden);
            request.setAttribute("resultados", resultados);
        } catch (WebServiceException e) {
            request.setAttribute("resultados", Collections.emptyList());
            request.setAttribute("mensajeError",
                    "No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        request.setAttribute("busquedaActual", query);
        request.setAttribute("filtroActual", filtro);
        request.setAttribute("ordenActual", orden);

        request.getRequestDispatcher("/componentes/resultadoBusqueda.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }
}