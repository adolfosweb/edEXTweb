package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String contexto = request.getContextPath();
        String rutaCookie = contexto.isEmpty() ? "/" : contexto;
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("recordarme".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                    try {
                        String hash = UtilToken.calcularHash(cookie.getValue());
                        EdextWebService servicio = ClienteSOAP.obtenerServicio();
                        servicio.eliminarTokenRecordarme(hash);
                    } catch (WebServiceException e) {
                        // Aunque el servidor central no esté disponible,
                        // igualmente se cerrará la sesión local y se borrará la cookie.
                        getServletContext().log("No se pudo eliminar el token de recordarme en el servidor central.",e);
                    }
                    break;
                }
            }
        }

        // Eliminar la cookie del navegador.
        Cookie eliminar = new Cookie("recordarme", "");
        eliminar.setMaxAge(0);
        eliminar.setPath(rutaCookie);
        eliminar.setHttpOnly(true);
        eliminar.setSecure(request.isSecure());
        eliminar.setAttribute("SameSite", "Lax");

        response.addCookie(eliminar);

        // Invalidar la sesión actual.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(contexto + "/componentes/principal.jsp");
    }
}
