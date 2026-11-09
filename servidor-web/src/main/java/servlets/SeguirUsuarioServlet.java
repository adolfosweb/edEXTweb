package servlets;

import WebServices.ClienteSOAP;
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

@WebServlet("/SeguirUsuarioServlet")
public class SeguirUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);
        if (sesion == null ||
            !(sesion.getAttribute("usuario") instanceof UsuarioDTO usuarioSesion)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String nicknameSeguido = request.getParameter("nickname");
        String accion = request.getParameter("accion");

        if (nicknameSeguido == null || nicknameSeguido.trim().isEmpty() || accion == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String nicknameSeguidor = usuarioSesion.getNickname();

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            if ("seguir".equals(accion)) {
                servicio.seguirUsuario(nicknameSeguidor,nicknameSeguido);

            } else if ("dejarDeSeguir".equals(accion)) {
                servicio.dejarDeSeguir(nicknameSeguidor,nicknameSeguido);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String destino = request.getContextPath()
                    + "/PerfilUsuarioServlet?nickname="
                    + URLEncoder.encode(nicknameSeguido,StandardCharsets.UTF_8);
            response.sendRedirect(destino);

        } catch (WebServiceException e) {
            getServletContext().log("Error al modificar el seguimiento mediante SOAP.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"No se pudo conectar con el servidor central.");
        }
    }
}
