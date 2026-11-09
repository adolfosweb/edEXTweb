package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoEdicionFormularioDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

@WebServlet(name = "SvltAltaEdicionCurso", urlPatterns = {"/SvltAltaEdicionCurso"})
public class AltaEdicionCursoServlet extends HttpServlet {

    private static final String JSP = "/componentes/cursos/altaEdicionCurso.jsp";

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {

        UsuarioDTO docente = obtenerDocente(request, response);
        if (docente == null) {
            return;
        }

        String nombreCurso = limpiar(request.getParameter("curso"));
        boolean modoEdicion = "editar".equals(request.getParameter("modo"));
        String nombreEdicion = limpiar(request.getParameter("edicion"));

        try {
            CursoEdicionFormularioDTO formulario =
                    ClienteSOAP.obtenerServicio()
                            .obtenerFormularioAltaEdicionCurso(
                                    docente.getNickname(),
                                    nombreCurso,
                                    modoEdicion,
                                    nombreEdicion);
            if (formulario == null || !formulario.isCursoEncontrado()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el curso.");
                return;
            }
            if (!formulario.isAutorizado()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No puedes gestionar ediciones de este curso.");
                return;
            }
            if (modoEdicion && !formulario.isEdicionEncontrada()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró la edición o no pertenece al curso.");
                return;
            }

            prepararAtributos(request, formulario);
            request.getRequestDispatcher(JSP).forward(request, response);
        } catch (WebServiceException ex) {
            getServletContext().log("Error al cargar el formulario de edición de curso.", ex);

            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servidor central.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        UsuarioDTO docente = obtenerDocente(request, response);
        if (docente == null) {
            return;
        }
        
        String nombreCurso = limpiar(request.getParameter("curso"));
        String nombreEdicion = limpiar(request.getParameter("nombre"));
        String inicio = limpiar(request.getParameter("fechaInicio"));
        String fin = limpiar(request.getParameter("fechaFin"));
        String cupo = limpiar(request.getParameter("cupo"));
        String edicionOriginal = limpiar(request.getParameter("edicionOriginal"));

        boolean modoEdicion = "editar".equals(request.getParameter("modo"));

        String[] docentesArray = request.getParameterValues("docentes");
        Set<String> docentes = docentesArray == null
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(java.util.Arrays.asList(docentesArray));

        String error = null;
        try {
            ResultadoOperacionDTO resultado =
                    ClienteSOAP.obtenerServicio().guardarEdicionCurso(
                            docente.getNickname(),
                            nombreCurso,
                            nombreEdicion,
                            inicio,
                            fin,
                            cupo,
                            modoEdicion,
                            edicionOriginal,
                            new ArrayList<>(docentes));

            if (resultado == null) {
                error = "El servidor central no devolvió un resultado.";
            } else if (!resultado.isExitoso()) {
                error = resultado.getMensaje();
            }
        } catch (WebServiceException ex) {
            getServletContext().log("Error al guardar la edición mediante SOAP.", ex);

            error = "No se pudo conectar con el servidor central.";
        }
        if (error == null) {
            response.sendRedirect(request.getContextPath()
                    + "/SvltConsultarCurso?nombre="
                    + URLEncoder.encode(nombreCurso, StandardCharsets.UTF_8));
            return;
        }
        // Recuperar los datos de referencia del formulario.
        try {
            CursoEdicionFormularioDTO formulario =
                    ClienteSOAP.obtenerServicio()
                            .obtenerFormularioAltaEdicionCurso(
                                    docente.getNickname(),
                                    nombreCurso,
                                    modoEdicion,
                                    edicionOriginal);

            if (formulario == null || !formulario.isCursoEncontrado()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el curso.");
                return;
            }

            if (!formulario.isAutorizado()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No puedes gestionar ediciones de este curso.");
                return;
            }

            if (modoEdicion && !formulario.isEdicionEncontrada()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró la edición.");
                return;
            }

            prepararAtributos(request, formulario);

            // Conservar los valores ingresados.
            request.setAttribute("nombreEdicion", nombreEdicion);
            request.setAttribute("fechaInicio", inicio);
            request.setAttribute("fechaFin", fin);
            request.setAttribute("cupo", cupo);
            request.setAttribute("edicionOriginal", edicionOriginal);
            request.setAttribute("error", error);
            request.setAttribute("docentesSeleccionados", docentes);

            request.getRequestDispatcher(JSP).forward(request, response);

        } catch (WebServiceException ex) {
            getServletContext().log("Error al recuperar el formulario después de un fallo.", ex);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo recuperar el formulario desde el servidor central.");
        }
    }

    private void prepararAtributos(HttpServletRequest request,CursoEdicionFormularioDTO formulario) {

        request.setAttribute("curso", formulario.getNombreCurso());
        request.setAttribute("docentes", formulario.getDocente());
        request.setAttribute("docentesSeleccionados",new LinkedHashSet<>(formulario.getDocenteSeleccionado()));
        request.setAttribute("modoEdicion", formulario.isModoEdicion());

        if (formulario.isModoEdicion()) {
            request.setAttribute("nombreEdicion",formulario.getNombreEdicion());
            request.setAttribute("fechaInicio",formulario.getFechaInicio());
            request.setAttribute("fechaFin",formulario.getFechaFin());
            request.setAttribute("cupo", formulario.getCupo());
            request.setAttribute("edicionOriginal",formulario.getNombreEdicion());
        }
    }

    private UsuarioDTO obtenerDocente(HttpServletRequest request,HttpServletResponse response) throws IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return null;
        }

        Object objeto = sesion.getAttribute("usuario");
        if (!(objeto instanceof UsuarioDTO usuario)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo un docente puede gestionar ediciones de cursos.");
            return null;
        }
        if (!"DOCENTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Solo un docente puede gestionar ediciones de cursos.");
            return null;
        }
        return usuario;
    }
    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}