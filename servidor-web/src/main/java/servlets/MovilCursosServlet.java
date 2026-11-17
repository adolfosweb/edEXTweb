package servlets;

import WebServices.ClienteSOAP;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@WebServlet(name = "MovilCursosServlet", urlPatterns = {"/movil/cursos"})
public class MovilCursosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CursoListadoDTO> cursos = new ArrayList<>();
        TreeSet<String> institutos = new TreeSet<>();
        Map<String, String> imagenes = new HashMap<>();
        String elegido = request.getParameter("instituto");
        if (elegido != null && elegido.isBlank()) {
            elegido = null;
        }

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            // Sin sesión de web todavía: se pide el listado sin usuario (igual que un visitante).
            ListadoCursosDTO listado = servicio.listarCursosParaWeb(null);
            List<CursoListadoDTO> todos = listado != null ? listado.getCursos() : Collections.emptyList();

            for (CursoListadoDTO c : todos) {
                if (c.getInstituto() != null) {
                    institutos.add(c.getInstituto());
                }
                if (elegido == null || elegido.equals(c.getInstituto())) {
                    cursos.add(c);
                }
            }

            // Imagen de cada curso que se va a mostrar.
            for (CursoListadoDTO c : cursos) {
                imagenes.put(c.getNombre(),
                        ImagenesCurso.urlImagen(servicio, c.getNombre(), request.getContextPath()));
            }
        } catch (WebServiceException e) {
            request.setAttribute("mensajeError",
                    "No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        request.setAttribute("cursos", cursos);
        request.setAttribute("institutos", institutos);
        request.setAttribute("institutoElegido", elegido);
        request.setAttribute("imagenes", imagenes);

        request.getRequestDispatcher("/movil/componentes/cursos.jsp").forward(request, response);
    }
}