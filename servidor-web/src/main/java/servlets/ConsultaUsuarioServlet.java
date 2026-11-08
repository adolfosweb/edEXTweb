
package com.mycompany.edextweb;

import Controladores.ControladorUsuario;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ConsultaUsuarioServlet",
            urlPatterns = {"/ConsultaUsuarioServlet"})
public class ConsultaUsuarioServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Usuario> usuarios =
                controladorUsuario.listarUsuarios();

        request.setAttribute("usuarios", usuarios);

        request.getRequestDispatcher(
                "/componentes/consultaUsuario.jsp"
        ).forward(request, response);
    }
}
