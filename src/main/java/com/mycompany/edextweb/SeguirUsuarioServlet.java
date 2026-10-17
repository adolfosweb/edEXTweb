package com.mycompany.edextweb;

import Controladores.ControladorSeguimiento;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/SeguirUsuarioServlet")
public class SeguirUsuarioServlet extends HttpServlet {

    private final ControladorSeguimiento controlador =
            new ControladorSeguimiento();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        if (sesion == null ||
            !(sesion.getAttribute("usuario") instanceof Usuario usuarioSesion)) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String nicknameSeguido = request.getParameter("nickname");
        String accion = request.getParameter("accion");

        if (nicknameSeguido == null || accion == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String nicknameSeguidor = usuarioSesion.getNickname();

        if ("seguir".equals(accion)) {
            controlador.seguir(nicknameSeguidor, nicknameSeguido);

        } else if ("dejarDeSeguir".equals(accion)) {
            controlador.dejarDeSeguir(nicknameSeguidor, nicknameSeguido);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String destino = request.getContextPath()
                + "/PerfilUsuarioServlet?nickname="
                + URLEncoder.encode(
                    nicknameSeguido,
                    StandardCharsets.UTF_8
                );

        response.sendRedirect(destino);
    }
}
