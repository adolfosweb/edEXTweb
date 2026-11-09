package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.InscripcionCursoDTO;
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

@WebServlet(name = "ResultadosInscripcionesServlet",urlPatterns = {"/resultadosInscripciones"})
public class ResultadosInscripcionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof UsuarioDTO)) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return;
        }
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (!"ESTUDIANTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Solo los estudiantes pueden consultar sus resultados.");
            return;
        }

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            List<InscripcionCursoDTO> inscripciones = servicio.listarInscripcionesEstudiante(usuario.getNickname());
            request.setAttribute("inscripciones",
                    inscripciones != null ? inscripciones : Collections.emptyList());
        } catch (WebServiceException e) {
            request.setAttribute("inscripciones",Collections.emptyList());
            request.setAttribute("mensajeError",
                    "No se pudieron consultar las inscripciones. Intente nuevamente.");
        }
        request.getRequestDispatcher("/componentes/resultadoInscripciones.jsp").forward(request, response);
    }
}
