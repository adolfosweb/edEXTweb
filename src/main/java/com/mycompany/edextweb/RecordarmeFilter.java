
package com.mycompany.edextweb;

import Controladores.ControladorTokenRecordarme;
import Controladores.ControladorUsuario;
import Logica.TokenRecordarme;
import Logica.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class RecordarmeFilter implements Filter {

    private ControladorTokenRecordarme controladorToken;
    private ControladorUsuario controladorUsuario;

    @Override
    public void init(FilterConfig filterConfig) {
        controladorToken = new ControladorTokenRecordarme();
        controladorUsuario = new ControladorUsuario();
    }

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Si ya hay una sesión con usuario, no hace falta recuperarla.
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {

            Cookie[] cookies = req.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {

                    if ("recordarme".equals(cookie.getName())
                            && cookie.getValue() != null
                            && !cookie.getValue().isBlank()) {

                        String hash = UtilToken.calcularHash(
                                cookie.getValue()
                        );

                        TokenRecordarme registro =
                                controladorToken.buscarTokenValido(hash);

                        if (registro != null) {

                            Usuario usuario = controladorUsuario.buscarUsuario(
                                    registro.getNicknameUsuario()
                            );

                            if (usuario != null) {
                                HttpSession nuevaSesion = req.getSession(true);
                                nuevaSesion.setAttribute("usuario", usuario);
                            } else {
                                eliminarCookie(req, res);
                                controladorToken.eliminarToken(hash);
                            }
                        }

                        break;
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    private void eliminarCookie(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("recordarme", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath(
                request.getContextPath().isEmpty()
                        ? "/" : request.getContextPath()
        );
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }
}
