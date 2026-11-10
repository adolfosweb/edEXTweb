package servlets;

import WebServices.ClienteSOAP;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;

@WebServlet(name = "MovilLogoutServlet", urlPatterns = {"/movil/logout"})
public class MovilLogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contexto = request.getContextPath();
        String rutaCookie = contexto.isEmpty() ? "/" : contexto;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("recordarme".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                    try {
                        ClienteSOAP.obtenerServicio()
                                .eliminarTokenRecordarme(UtilToken.calcularHash(cookie.getValue()));
                    } catch (WebServiceException e) {
                        // Aunque el servidor central no responda, igual se cierra la sesión local.
                        getServletContext().log("No se pudo eliminar el token de recordarme.", e);
                    }
                    break;
                }
            }
        }

        Cookie eliminar = new Cookie("recordarme", "");
        eliminar.setMaxAge(0);
        eliminar.setPath(rutaCookie);
        eliminar.setHttpOnly(true);
        eliminar.setSecure(request.isSecure());
        eliminar.setAttribute("SameSite", "Lax");
        response.addCookie(eliminar);

        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        response.sendRedirect(contexto + "/movil/login");
    }
}