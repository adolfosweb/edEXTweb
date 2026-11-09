package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.UsuarioDTO;
import com.mycompany.edextweb.soap.InstitutoDTO;
import com.mycompany.edextweb.soap.CursoInscripcionDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InscripcionEdicionServlet", urlPatterns = {"/inscripcionEdicion"})
public class InscripcionEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        UsuarioDTO usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Debes iniciar sesión para acceder a esta página.");
            return;
        }
        mostrarPagina(request, response, request.getParameter("instituto"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        UsuarioDTO usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Debes iniciar sesión para inscribirte.");
            return;
        }

        String nombreInstituto = request.getParameter("instituto");
        String nombreEdicion = request.getParameter("edicion");

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            ResultadoOperacionDTO resultado = servicio.inscribirEstudiante(
                    usuario.getNickname(),
                    nombreInstituto,
                    nombreEdicion
            );

            request.setAttribute("exitoso", resultado.isExitoso());
            request.setAttribute("mensaje", resultado.getMensaje());

            mostrarPagina(request, response, nombreInstituto);

        } catch (WebServiceException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo comunicar con el servidor central.");
        }
    }

    private UsuarioDTO obtenerUsuarioSesion(HttpServletRequest request) {

        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            return null;
        }

        Object usuario = sesion.getAttribute("usuario");
        if (!(usuario instanceof UsuarioDTO)) {
            return null;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) usuario;
        if (!"ESTUDIANTE".equals(usuarioDTO.getTipoUsuario())) {
            return null;
        }
        return usuarioDTO;
    }

    private void mostrarPagina(HttpServletRequest request,HttpServletResponse response, String nombreInstituto)throws ServletException, IOException {

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            List<InstitutoDTO> institutos = servicio.listarInstitutos();
            request.setAttribute("institutos", institutos);
            InstitutoDTO institutoSeleccionado = null;

            if (nombreInstituto != null && !nombreInstituto.isBlank()) {
                for (InstitutoDTO instituto : institutos) {
                    if (nombreInstituto.equals(instituto.getNombre())) {
                        institutoSeleccionado = instituto;
                        break;
                    }
                }
            }

            request.setAttribute("institutoSeleccionado", institutoSeleccionado);
            List<CursoInscripcionDTO> cursos = List.of();

            if (institutoSeleccionado != null) {
                cursos = servicio.listarCursosConEdicionesPorInstituto(institutoSeleccionado.getNombre());
            }

            request.setAttribute("cursos", cursos);
            request.getRequestDispatcher("/componentes/inscripcionEdicion.jsp").forward(request, response);

        } catch (WebServiceException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo obtener la información del servidor central.");
        }
    }
}
