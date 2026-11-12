package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoTendenciaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Listado de tendencias: cursos más visitados, de mayor a menor (requerimiento 7.3). */
@WebServlet(name = "SvltTendencias", urlPatterns = {"/SvltTendencias"})
public class TendenciasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CursoTendenciaDTO> cursos = new ArrayList<>();
        try {
            cursos.addAll(ClienteSOAP.obtenerServicio().listarCursosMasVisitados());
            cursos.sort(Comparator.comparingInt(CursoTendenciaDTO::getVisitas).reversed());
        } catch (WebServiceException e) {
            getServletContext().log("Error al listar las tendencias mediante SOAP.", e);
            request.setAttribute("mensajeError",
                    "No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        request.setAttribute("cursos", cursos);
        request.getRequestDispatcher("/componentes/tendencias.jsp").forward(request, response);
    }
}