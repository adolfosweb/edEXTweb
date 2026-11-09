package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ProgramaConsultaDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
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

@WebServlet(name = "InscripcionProgramaServlet",urlPatterns = {"/inscripcionPrograma"})
public class InscripcionProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        mostrarProgramas(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        String contexto = request.getContextPath();
        // Comprobar que exista una sesión válida.
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("usuario") instanceof UsuarioDTO)) {
            response.sendRedirect(contexto + "/componentes/login.jsp");
            return;
        }
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        // Obtener el programa seleccionado.
        String nombrePrograma = request.getParameter("programa");

        if (nombrePrograma == null || nombrePrograma.isBlank()) {
            request.setAttribute("error", "No se especificó el programa.");
            mostrarProgramas(request, response);
            return;
        }

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            ResultadoOperacionDTO resultado =
                    servicio.inscribirEstudiantePrograma(
                            usuario.getNickname(),
                            nombrePrograma);

            if (resultado != null && resultado.isExitoso()) {
                request.setAttribute("mensaje", resultado.getMensaje());
            } else {
                request.setAttribute("error",
                        resultado != null ? resultado.getMensaje() : "No se pudo completar la inscripción.");
            }
        } catch (WebServiceException e) {
            request.setAttribute("error","No se pudo conectar con el servidor central. Intente nuevamente.");
        }
        mostrarProgramas(request, response);
    }

    private void mostrarProgramas(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            List<ProgramaConsultaDTO> programas = servicio.listarProgramasConsulta();
            request.setAttribute("programas",programas != null ? programas : Collections.emptyList());

        } catch (WebServiceException e) {
            request.setAttribute("programas", Collections.emptyList());
            request.setAttribute("errorCarga","No se pudieron cargar los programas. Intente nuevamente.");
        }

        request.getRequestDispatcher("/componentes/inscripcionPrograma.jsp").forward(request, response);
    }
}
