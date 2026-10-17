package com.mycompany.edextweb;

import Controladores.ControladorUsuario;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "PerfilUsuarioServlet",
            urlPatterns = {"/PerfilUsuarioServlet"})
public class PerfilUsuarioServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String nickname = request.getParameter("nickname");

        if (nickname == null || nickname.trim().isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "No se indicó el usuario."
            );
            return;
        }

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);

        if (usuario == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "No se encontró el usuario."
            );
            return;
        }

        // Guardar el usuario que se está consultando
        request.setAttribute("usuario", usuario);

        // Comprobar si el usuario está viendo su propio perfil
        HttpSession session = request.getSession(false);
        boolean esPropioPerfil = false;

        if (session != null) {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            if (usuarioSesion != null
                    && usuarioSesion.getNickname() != null) {
                esPropioPerfil = usuarioSesion.getNickname()
                        .equals(usuario.getNickname());
            }
        }

        request.setAttribute("esPropioPerfil", esPropioPerfil);

        request.getRequestDispatcher(
                "/componentes/perfilUsuario.jsp"
        ).forward(request, response);
    }
}