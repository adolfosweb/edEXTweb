package servlets;

import com.mycompany.edextweb.soap.CursoProgramaDTO;
import com.mycompany.edextweb.soap.EdicionCursoDTO;
import com.mycompany.edextweb.soap.InscripcionSeleccionDTO;
import com.mycompany.edextweb.soap.InstitutoDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.UsuarioDTO;
import WebServices.ClienteSOAP;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SeleccionarEstudiantesServlet",urlPatterns = {"/seleccionarEstudiantes"})
public class SeleccionarEstudiantesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null
                || !(session.getAttribute("usuario") instanceof UsuarioDTO usuario)
                || !"DOCENTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los docentes pueden gestionar inscripciones.");
            return;
        }

        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            List<InstitutoDTO> institutos = servicio.listarInstitutosDocente(usuario.getNickname());
            request.setAttribute("institutos", institutos);
            if (institutoNombre != null && !institutoNombre.isBlank()) {
                request.setAttribute("institutoSeleccionado", institutoNombre);
                List<CursoProgramaDTO> cursos = servicio.listarCursosDocente(usuario.getNickname(), institutoNombre);

                request.setAttribute("cursos", cursos);
                if (cursoNombre != null && !cursoNombre.isBlank()) {
                    request.setAttribute("cursoSeleccionado", cursoNombre);
                    List<EdicionCursoDTO> ediciones =
                            servicio.listarEdicionesDocente(
                                    usuario.getNickname(),
                                    institutoNombre,
                                    cursoNombre);

                    request.setAttribute("ediciones", ediciones);
                    if (edicionNombre != null && !edicionNombre.isBlank()) {
                        boolean edicionPermitida = ediciones != null && ediciones.stream().anyMatch(e -> edicionNombre.equals(e.getNombre()));
                        if (!edicionPermitida) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,"No puedes gestionar esa edición.");
                            return;
                        }
                        List<InscripcionSeleccionDTO> inscripciones =
                                servicio.listarInscripcionesEdicionDocente(
                                        usuario.getNickname(),
                                        institutoNombre,
                                        cursoNombre,
                                        edicionNombre);
                        request.setAttribute("inscripciones", inscripciones);
                        request.setAttribute("edicionSeleccionada", edicionNombre);
                    }
                }
            }
        } catch (WebServiceException e) {
            log("Error al consultar las inscripciones de estudiantes.", e);
            request.setAttribute("mensajeError","No se pudieron cargar las inscripciones. Inténtalo nuevamente.");
            request.setAttribute("institutos", new ArrayList<>());
        }
        Object mensaje = session.getAttribute("mensajeSeleccion");
        if (mensaje != null) {
            request.setAttribute("mensajeSeleccion", mensaje);
            session.removeAttribute("mensajeSeleccion");
        }
        request.getRequestDispatcher("/componentes/seleccionarEstudiantes.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null
                || !(session.getAttribute("usuario") instanceof UsuarioDTO usuario)
                || !"DOCENTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los docentes pueden gestionar inscripciones.");
            return;
        }

        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");
        String idTexto = request.getParameter("idInscripcion");
        String estadoTexto = request.getParameter("estado");

        if (institutoNombre == null || institutoNombre.isBlank()
                || cursoNombre == null || cursoNombre.isBlank()
                || edicionNombre == null || edicionNombre.isBlank()
                || idTexto == null || idTexto.isBlank()) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Faltan datos para gestionar la inscripción.");
            return;
        }

        if (!"ACEPTADA".equals(estadoTexto) && !"RECHAZADA".equals(estadoTexto)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"El estado seleccionado no es válido.");
            return;
        }

        try {
            Long idInscripcion = Long.valueOf(idTexto);
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            ResultadoOperacionDTO resultado =
                    servicio.cambiarEstadoInscripcionDocente(
                            usuario.getNickname(),
                            institutoNombre,
                            cursoNombre,
                            edicionNombre,
                            idInscripcion,
                            estadoTexto);
            session.setAttribute("mensajeSeleccion",resultado != null
                            ? resultado.getMensaje() : "No se recibió una respuesta válida del servidor.");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"El identificador de inscripción no es válido.");
            return;
        } catch (WebServiceException e) {
            log("Error al cambiar el estado de la inscripción.", e);
            session.setAttribute("mensajeSeleccion","No se pudo comunicar con el servidor central.");
        }
        String url = request.getContextPath()
                + "/seleccionarEstudiantes"
                + "?instituto=" + URLEncoder.encode(institutoNombre, StandardCharsets.UTF_8)
                + "&curso=" + URLEncoder.encode(cursoNombre, StandardCharsets.UTF_8)
                + "&edicion=" + URLEncoder.encode(edicionNombre, StandardCharsets.UTF_8);
        response.sendRedirect(url);
    }
}