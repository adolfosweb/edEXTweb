package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoProgramaDTO;
import com.mycompany.edextweb.soap.EdicionCursoDTO;
import com.mycompany.edextweb.soap.EvaluacionDTO;
import com.mycompany.edextweb.soap.InscripcionSeleccionDTO;
import com.mycompany.edextweb.soap.InstitutoDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "IngresarEvaluacionesServlet",urlPatterns = {"/ingresarEvaluaciones"})
public class IngresarEvaluacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null
                || !(session.getAttribute("usuario") instanceof UsuarioDTO usuario)
                || !"DOCENTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los docentes pueden ingresar evaluaciones.");
            return;
        }
        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");
        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            List<InstitutoDTO> institutos = servicio.listarInstitutosDocente(usuario.getNickname());
            request.setAttribute("institutos", institutos);
            if(institutoNombre != null && !institutoNombre.isBlank()) {
                
                request.setAttribute("institutoSeleccionado", institutoNombre);
                List<CursoProgramaDTO> cursos = servicio.listarCursosDocente(usuario.getNickname(), institutoNombre);
                request.setAttribute("cursos", cursos);
                if(cursoNombre != null && !cursoNombre.isBlank()) {
                    request.setAttribute("cursoSeleccionado", cursoNombre);
                    List<EdicionCursoDTO> ediciones =
                            servicio.listarEdicionesDocente(
                                    usuario.getNickname(),
                                    institutoNombre,
                                    cursoNombre);
                    request.setAttribute("ediciones", ediciones);

                    if(edicionNombre != null && !edicionNombre.isBlank()) {

                        boolean edicionPermitida = ediciones != null
                                && ediciones.stream().anyMatch(e -> edicionNombre.equals(e.getNombre()));
                        if (!edicionPermitida) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "No puedes gestionar esa edición.");
                            return;
                        }
                        EdicionCursoDTO edicionSeleccionada = ediciones.stream()
                                .filter(e -> edicionNombre.equals(e.getNombre()))
                                .findFirst()
                                .orElse(null);

                        List<InscripcionSeleccionDTO> inscripciones =
                                servicio.listarInscripcionesEdicionDocente(
                                        usuario.getNickname(),
                                        institutoNombre,
                                        cursoNombre,
                                        edicionNombre);

                        List<InscripcionSeleccionDTO> aceptados = new ArrayList<>();
                        if(inscripciones != null) {
                            for(InscripcionSeleccionDTO inscripcion : inscripciones) {
                                if("ACEPTADA".equals(inscripcion.getEstado())) {
                                    aceptados.add(inscripcion);
                                }
                            }
                        }
                        request.setAttribute("inscripciones", aceptados);
                        request.setAttribute("edicionSeleccionada", edicionNombre);
                        request.setAttribute("edicionSeleccionadaDTO", edicionSeleccionada);
                    }
                }
            }
        } catch (WebServiceException e) {
            log("Error al cargar las evaluaciones.", e);
            request.setAttribute("mensajeError","No se pudieron cargar los datos. Inténtalo nuevamente.");
            request.setAttribute("institutos", new ArrayList<>());
        }
        Object mensaje = session.getAttribute("mensajeEvaluaciones");
        if(mensaje != null) {
            request.setAttribute("mensajeEvaluaciones", mensaje);
            session.removeAttribute("mensajeEvaluaciones");
        }
        request.getRequestDispatcher("/componentes/ingresarEvaluaciones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null
                || !(session.getAttribute("usuario") instanceof UsuarioDTO usuario)
                || !"DOCENTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo los docentes pueden ingresar evaluaciones.");
            return;
        }
        String institutoNombre = request.getParameter("instituto");
        String cursoNombre = request.getParameter("curso");
        String edicionNombre = request.getParameter("edicion");
        String accion = request.getParameter("accion");
        if(institutoNombre == null || institutoNombre.isBlank()
                || cursoNombre == null || cursoNombre.isBlank()
                || edicionNombre == null || edicionNombre.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Faltan datos para gestionar las evaluaciones.");
            return;
        }
        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            ResultadoOperacionDTO resultado;

            if("guardar".equals(accion)) {
                List<InscripcionSeleccionDTO> inscripciones =
                        servicio.listarInscripcionesEdicionDocente(
                                usuario.getNickname(),
                                institutoNombre,
                                cursoNombre,
                                edicionNombre);

                List<EvaluacionDTO> evaluaciones = new ArrayList<>();
                if(inscripciones != null) {
                    for(InscripcionSeleccionDTO inscripcion : inscripciones) {

                        if(!"ACEPTADA".equals(inscripcion.getEstado())) {
                            continue;
                        }
                        String notaTexto = request.getParameter("nota_" + inscripcion.getId());
                        // Permite guardar solamente las notas que se completaron.
                        if(notaTexto == null || notaTexto.isBlank()) {
                            continue;
                        }
                        int nota = Integer.parseInt(notaTexto);
                        if(nota < 1 || nota > 12) {
                            session.setAttribute("mensajeEvaluaciones","Las notas deben estar comprendidas entre 1 y 12.");
                            redirigir(response, request, institutoNombre,cursoNombre, edicionNombre);
                            return;
                        }
                        EvaluacionDTO evaluacion = new EvaluacionDTO();
                        evaluacion.setIdInscripcion(inscripcion.getId());
                        evaluacion.setNota(nota);
                        evaluaciones.add(evaluacion);
                    }
                }
                if(evaluaciones.isEmpty()) {
                    session.setAttribute("mensajeEvaluaciones","Ingresá al menos una nota para guardar.");
                    redirigir(response, request, institutoNombre,cursoNombre, edicionNombre);
                    return;
                }
                resultado = servicio.guardarEvaluacionesDocente(
                        usuario.getNickname(),
                        institutoNombre,
                        cursoNombre,
                        edicionNombre,
                        evaluaciones);

            } else if("cerrar".equals(accion)) {
                resultado = servicio.cerrarEdicionDocente(
                        usuario.getNickname(),
                        institutoNombre,
                        cursoNombre,
                        edicionNombre);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"La acción solicitada no es válida.");
                return;
            }
            session.setAttribute("mensajeEvaluaciones",
                    resultado != null ? resultado.getMensaje() : "No se recibió una respuesta válida del servidor.");

        } catch (NumberFormatException e) {
            session.setAttribute("mensajeEvaluaciones","Una de las notas ingresadas no es un número válido.");
        } catch (WebServiceException e) {
            log("Error al guardar las evaluaciones.", e);
            session.setAttribute("mensajeEvaluaciones","No se pudo comunicar con el servidor central.");
        }
        redirigir(response, request, institutoNombre,cursoNombre, edicionNombre);
    }

    private void redirigir(HttpServletResponse response,HttpServletRequest request,
                           String instituto,
                           String curso,
                           String edicion) throws IOException {

        String url = request.getContextPath()
                + "/ingresarEvaluaciones"
                + "?instituto=" + URLEncoder.encode(instituto, StandardCharsets.UTF_8)
                + "&curso=" + URLEncoder.encode(curso, StandardCharsets.UTF_8)
                + "&edicion=" + URLEncoder.encode(edicion, StandardCharsets.UTF_8);
        response.sendRedirect(url);
    }
}