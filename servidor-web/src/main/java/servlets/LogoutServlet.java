
package com.mycompany.edextweb;

import Controladores.ControladorTokenRecordarme;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    private ControladorTokenRecordarme controladorToken;

    @Override
    public void init() throws ServletException {
        controladorToken = new ControladorTokenRecordarme();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String contexto = request.getContextPath();
        String rutaCookie = contexto.isEmpty() ? "/" : contexto;

        // Eliminar el token persistente, si existe
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if ("recordarme".equals(cookie.getName())
                        && cookie.getValue() != null
                        && !cookie.getValue().isBlank()) {

                    String hash = UtilToken.calcularHash(
                            cookie.getValue()
                    );

                    controladorToken.eliminarToken(hash);
                    break;
                }
            }
        }

        // Borrar la cookie del navegador
        Cookie eliminar = new Cookie("recordarme", "");
        eliminar.setMaxAge(0);
        eliminar.setPath(rutaCookie);
        eliminar.setHttpOnly(true);
        eliminar.setSecure(request.isSecure());
        eliminar.setAttribute("SameSite", "Lax");

        response.addCookie(eliminar);

        // Invalidar la sesión normal
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(
                contexto + "/componentes/principal.jsp"
        );
    }
}
