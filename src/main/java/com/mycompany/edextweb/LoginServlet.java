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

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identificador = request.getParameter("identificador");
        String password = request.getParameter("password");

        Usuario usuario = controladorUsuario.iniciarSesion(
                identificador,
                password
        );

        if (usuario != null) {

            HttpSession session = request.getSession();

            session.setAttribute("usuario", usuario);

            response.sendRedirect(request.getContextPath() + "/componentes/principal.jsp");

        } else {

            request.setAttribute(
                    "error",
                    "Usuario o contraseña incorrectos."
            );

            request.getRequestDispatcher("/componentes/login.jsp").forward(request, response);
        }
    }
}