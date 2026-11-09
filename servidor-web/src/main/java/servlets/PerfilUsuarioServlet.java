package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.PerfilUsuarioDTO;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;

@WebServlet(name = "PerfilUsuarioServlet",urlPatterns = {"/PerfilUsuarioServlet"})
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {

        String nickname = request.getParameter("nickname");

        if (nickname == null || nickname.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"No se indicó el usuario.");
            return;
        }

        try {
            // =====================================
            // CONSULTAR PERFIL MEDIANTE SOAP
            // =====================================

            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            PerfilUsuarioDTO perfil = servicio.consultarPerfilUsuario(nickname);

            if (perfil == null || perfil.getUsuario() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,"No se encontró el usuario.");
                return;
            }
            UsuarioDTO usuario = perfil.getUsuario();

            // =====================================
            // DATOS DEL PERFIL
            // =====================================

            request.setAttribute("perfil", perfil);
            request.setAttribute("usuario", usuario);

            // =====================================
            // SESIÓN Y SEGUIMIENTO
            // =====================================

            HttpSession session = request.getSession(false);

            boolean esPropioPerfil = false;
            boolean siguiendo = false;

            UsuarioDTO usuarioSesion = null;

            if (session != null) {
                Object atributoUsuario = session.getAttribute("usuario");

                if (atributoUsuario instanceof UsuarioDTO) {
                    usuarioSesion = (UsuarioDTO) atributoUsuario;
                }
            }

            if (usuarioSesion != null && usuarioSesion.getNickname() != null) {

                esPropioPerfil = usuarioSesion.getNickname().equals(usuario.getNickname());

                if (!esPropioPerfil) {
                    siguiendo = servicio.estaSiguiendo(usuarioSesion.getNickname(),usuario.getNickname());
                }
            }

            request.setAttribute("esPropioPerfil", esPropioPerfil);
            request.setAttribute("siguiendo", siguiendo);

            // =====================================
            // MOSTRAR PERFIL
            // =====================================

            request.getRequestDispatcher("/componentes/perfilUsuario.jsp").forward(request, response);

        } catch (WebServiceException e) {
            getServletContext().log("Error al consultar el perfil mediante SOAP.",e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"No se pudo conectar con el servidor central.");
        }
    }
}
