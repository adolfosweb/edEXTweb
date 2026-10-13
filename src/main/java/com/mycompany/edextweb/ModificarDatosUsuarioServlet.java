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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(name = "ModificarDatosUsuarioServlet",
            urlPatterns = {"/ModificarDatosUsuarioServlet"})
public class ModificarDatosUsuarioServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
    }

    private String obtenerNicknameSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (usuarioSesion == null) {
            return null;
        }

        return usuarioSesion.getNickname();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String nickname = request.getParameter("nickname");
        String nicknameSesion = obtenerNicknameSesion(request);

        if (nicknameSesion == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Debés iniciar sesión para modificar tus datos."
            );
            return;
        }

        if (nickname == null || !nicknameSesion.equals(nickname)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "No podés modificar los datos de otro usuario."
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

        request.setAttribute("usuario", usuario);

        request.getRequestDispatcher(
                "/componentes/modificarDatosUsuario.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nickname = request.getParameter("nickname");
        String nicknameSesion = obtenerNicknameSesion(request);

        if (nicknameSesion == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Debés iniciar sesión para modificar tus datos."
            );
            return;
        }

        if (nickname == null || !nicknameSesion.equals(nickname)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "No puedes modificar los datos de otro usuario."
            );
            return;
        }

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String fechaTexto = request.getParameter("fechaNacimiento");

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);

        if (usuario == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "No se encontró el usuario."
            );
            return;
        }

        // Validar campos obligatorios.
        if (nombre == null || nombre.trim().isEmpty()
                || apellido == null || apellido.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || fechaTexto == null || fechaTexto.trim().isEmpty()) {

            mostrarError(request, response, usuario,
                    "Todos los campos son obligatorios.");
            return;
        }

        LocalDate fechaNacimiento;

        try {
            fechaNacimiento = LocalDate.parse(fechaTexto.trim());

            if (fechaNacimiento.isAfter(LocalDate.now())) {
                mostrarError(request, response, usuario,
                        "La fecha de nacimiento no puede ser futura.");
                return;
            }

        } catch (DateTimeParseException e) {
            mostrarError(request, response, usuario,
                    "La fecha de nacimiento no es válida.");
            return;
        }

        correo = correo.trim();

        if (!correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            mostrarError(request, response, usuario,
                    "Ingresá un correo electrónico válido.");
            return;
        }

        if (controladorUsuario.correoUsadoPorOtroUsuario(
                correo, nickname)) {
            mostrarError(request, response, usuario,
                    "Ese correo electrónico ya está registrado.");
            return;
        }

        // Actualizar solamente los datos permitidos.
        usuario.setNombre(nombre.trim());
        usuario.setApellido(apellido.trim());
        usuario.setCorreoElectronico(correo);
        usuario.setFecNac(fechaNacimiento);

        controladorUsuario.modificarUsuario(usuario);

        String destino = request.getContextPath()
                + "/PerfilUsuarioServlet?nickname="
                + URLEncoder.encode(nickname, StandardCharsets.UTF_8)
                + "&actualizado=1";

        response.sendRedirect(destino);
    }

    private void mostrarError(HttpServletRequest request,
                              HttpServletResponse response,
                              Usuario usuario,
                              String mensaje)
            throws ServletException, IOException {

        request.setAttribute("usuario", usuario);
        request.setAttribute("error", mensaje);

        request.getRequestDispatcher(
                "/componentes/modificarDatosUsuario.jsp"
        ).forward(request, response);
    }
}