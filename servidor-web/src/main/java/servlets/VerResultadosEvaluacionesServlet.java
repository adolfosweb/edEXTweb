
package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoProgramaDTO;
import com.mycompany.edextweb.soap.EdicionCursoDTO;
import com.mycompany.edextweb.soap.InscripcionSeleccionDTO;
import com.mycompany.edextweb.soap.InstitutoDTO;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "VerResultadosEvaluacionesServlet",urlPatterns = {"/verResultadosEvaluaciones"})
public class VerResultadosEvaluacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null
                || !(session.getAttribute("usuario") instanceof UsuarioDTO usuario)
                || !"DOCENTE".equals(usuario.getTipoUsuario())) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Solo los docentes pueden consultar resultados de evaluaciones.");
            return;
        }

        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            // 1. Obtener los institutos del docente.
            List<InstitutoDTO> institutos = servicio.listarInstitutosDocente(usuario.getNickname());
            if (institutos == null) {
                institutos = new ArrayList<>();
            }
            request.setAttribute("institutos", institutos);
            // 2. Obtener los cursos del instituto seleccionado.
            if(institutoNombre != null && !institutoNombre.isBlank()) {

                boolean institutoPermitido = institutos.stream().anyMatch(i -> institutoNombre.equals(i.getNombre()));

                if(!institutoPermitido) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "No puedes consultar resultados de ese instituto.");
                    return;
                }
                request.setAttribute("institutoSeleccionado", institutoNombre);
                List<CursoProgramaDTO> cursos =
                        servicio.listarCursosDocente(
                                usuario.getNickname(),
                                institutoNombre
                        );

                if(cursos == null) {
                    cursos = new ArrayList<>();
                }

                request.setAttribute("cursos", cursos);
                // 3. Obtener únicamente las ediciones cerradas del curso.
                if(cursoNombre != null && !cursoNombre.isBlank()) {
                    boolean cursoPermitido = cursos.stream().anyMatch(c -> cursoNombre.equals(c.getNombre()));
                    if(!cursoPermitido) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                "No puedes consultar resultados de ese curso.");
                        return;
                    }

                    request.setAttribute("cursoSeleccionado", cursoNombre);
                    List<EdicionCursoDTO> todasLasEdiciones =
                            servicio.listarEdicionesDocente(
                                    usuario.getNickname(),
                                    institutoNombre,
                                    cursoNombre
                            );

                    List<EdicionCursoDTO> edicionesCerradas = new ArrayList<>();
                    if(todasLasEdiciones != null) {
                        for(EdicionCursoDTO edicion : todasLasEdiciones) {
                            if(edicion != null && edicion.isCerrada()) {
                                edicionesCerradas.add(edicion);
                            }
                        }
                    }
                    request.setAttribute("ediciones", edicionesCerradas);
                    // 4. Consultar el acta de la edición seleccionada.
                    if(edicionNombre != null && !edicionNombre.isBlank()) {
                        EdicionCursoDTO edicionSeleccionada =
                                edicionesCerradas.stream()
                                        .filter(e -> edicionNombre.equals(e.getNombre()))
                                        .findFirst()
                                        .orElse(null);
                        if(edicionSeleccionada == null) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "Solo puedes consultar ediciones cerradas en las que participas.");
                            return;
                        }
                        List<InscripcionSeleccionDTO> inscripciones =
                                servicio.listarInscripcionesEdicionDocente(
                                        usuario.getNickname(),
                                        institutoNombre,
                                        cursoNombre,
                                        edicionNombre
                                );

                        List<InscripcionSeleccionDTO> participantes = new ArrayList<>();
                        if(inscripciones != null) {
                            for(InscripcionSeleccionDTO inscripcion : inscripciones) {
                                if(inscripcion != null && "ACEPTADA".equals(inscripcion.getEstado())) {
                                    participantes.add(inscripcion);
                                }
                            }
                        }
                        request.setAttribute("edicionSeleccionadaDTO",edicionSeleccionada);
                        request.setAttribute("edicionSeleccionada",edicionNombre);
                        request.setAttribute("inscripciones",participantes);
                    }
                }
            }
        } catch (WebServiceException e) {
            log("Error al consultar resultados de evaluaciones.", e);
            request.setAttribute("mensajeError","No se pudieron consultar los resultados. Inténtalo nuevamente.");
            request.setAttribute("institutos", new ArrayList<>());
        }
        request.getRequestDispatcher("/componentes/verResultadosEvaluaciones.jsp").forward(request, response);
    }
}