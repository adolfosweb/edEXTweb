package servlets;

import com.mycompany.edextweb.soap.PerfilUsuarioDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
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

@WebServlet(name = "ModificarDatosUsuarioServlet",urlPatterns = {"/ModificarDatosUsuarioServlet"})
public class ModificarDatosUsuarioServlet extends HttpServlet {

    private String obtenerNicknameSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        UsuarioDTO usuarioSesion = (UsuarioDTO) session.getAttribute("usuario");
        if (usuarioSesion == null) {
            return null;
        }
        return usuarioSesion.getNickname();
    }

    private boolean validarPermiso(HttpServletRequest request,HttpServletResponse response, String nickname)throws IOException {
        String nicknameSesion = obtenerNicknameSesion(request);
        if (nicknameSesion == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Debés iniciar sesión para modificar tus datos.");
            return false;
        }
        if (nickname == null || !nicknameSesion.equals(nickname)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No podés modificar los datos de otro usuario.");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        if (!validarPermiso(request, response, nickname)) {
            return;
        }
        try {
            cargarUsuario(request, nickname);
            if (request.getAttribute("usuario") == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el usuario.");
                return;
            }
            request.getRequestDispatcher("/componentes/modificarDatosUsuario.jsp").forward(request, response);
        } catch (WebServiceException e) {
            log("Error al consultar los datos del usuario.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudieron consultar los datos del usuario.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String nickname = request.getParameter("nickname");
        if (!validarPermiso(request, response, nickname)) {
            return;
        }
        try {
            ResultadoOperacionDTO resultado =
                    ClienteSOAP.obtenerServicio().modificarDatosUsuario(
                            nickname,
                            request.getParameter("nombre"),
                            request.getParameter("apellido"),
                            request.getParameter("correo"),
                            request.getParameter("fechaNacimiento"));
            if (resultado != null && resultado.isExitoso()) {
                String destino = request.getContextPath()
                        + "/PerfilUsuarioServlet?nickname="
                        + URLEncoder.encode(nickname, StandardCharsets.UTF_8)
                        + "&actualizado=1";
                response.sendRedirect(destino);
                return;
            }
            request.setAttribute("error",
                    resultado != null
                            ? resultado.getMensaje()
                            : "No se recibió una respuesta válida del servidor central.");
        } catch (WebServiceException e) {
            log("Error al comunicarse con el servidor central al modificar los datos.", e);
            request.setAttribute("error",
                    "No se pudo comunicar con el servidor central. Intente nuevamente.");
        }
        try {
            cargarUsuario(request, nickname);
            request.getRequestDispatcher("/componentes/modificarDatosUsuario.jsp").forward(request, response);
        } catch (WebServiceException e) {
            log("No se pudieron recuperar los datos del usuario para mostrar el formulario.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudieron recuperar los datos del usuario.");
        }
    }
    private void cargarUsuario(HttpServletRequest request, String nickname) {
        PerfilUsuarioDTO perfil = ClienteSOAP.obtenerServicio().consultarPerfilUsuario(nickname);
        if (perfil != null) {
            request.setAttribute("usuario", perfil.getUsuario());
        }
    }
}