
package com.mycompany.edextweb;

import Controladores.ControladorUsuario;
import Controladores.ControladorTokenRecordarme;
import Logica.Usuario;
import Logica.TokenRecordarme;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;
    private ControladorTokenRecordarme controladorToken;

    private static final int DURACION_COOKIE = 30 * 24 * 60 * 60;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
        controladorToken = new ControladorTokenRecordarme();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String identificador = request.getParameter("identificador");
        String password = request.getParameter("password");

        Usuario usuario = controladorUsuario.iniciarSesion(
                identificador, password
        );

        if (usuario != null) {

            // Crear la sesión normal.
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);

            String recordarme = request.getParameter("recordarme");

            if ("true".equals(recordarme)) {

                // Generar un token secreto.
                String token = UtilToken.generarToken();
                String hash = UtilToken.calcularHash(token);

                // Guardar solamente el hash en la base de datos.
                TokenRecordarme registro = new TokenRecordarme();
                registro.setTokenHash(hash);
                registro.setNicknameUsuario(usuario.getNickname());
                registro.setFechaExpiracion(
                        LocalDateTime.now().plusDays(30)
                );

                controladorToken.guardarToken(registro);

                // Enviar el token original al navegador.
                Cookie cookie = new Cookie("recordarme", token);
                cookie.setMaxAge(DURACION_COOKIE);
                cookie.setHttpOnly(true);
                cookie.setSecure(request.isSecure());
                cookie.setPath(request.getContextPath().isEmpty()
                        ? "/" : request.getContextPath());
                cookie.setAttribute("SameSite", "Lax");

                response.addCookie(cookie);

            } else {
                // Si no se marcó "Recordarme", eliminar una cookie anterior.
                Cookie[] cookies = request.getCookies();

                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("recordarme".equals(c.getName())) {
                            controladorToken.eliminarToken(
                                    UtilToken.calcularHash(c.getValue())
                            );

                            Cookie eliminar = new Cookie(
                                    "recordarme", ""
                            );
                            eliminar.setMaxAge(0);
                            eliminar.setPath(
                                    request.getContextPath().isEmpty()
                                            ? "/" : request.getContextPath()
                            );
                            eliminar.setHttpOnly(true);
                            eliminar.setSecure(request.isSecure());
                            eliminar.setAttribute("SameSite", "Lax");

                            response.addCookie(eliminar);
                            break;
                        }
                    }
                }
            }

            response.sendRedirect(
                    request.getContextPath() + "/componentes/principal.jsp"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Usuario o contraseña incorrectos."
            );

            request.getRequestDispatcher(
                    "/componentes/login.jsp"
            ).forward(request, response);
        }
    }
}
