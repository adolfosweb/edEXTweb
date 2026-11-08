
package com.mycompany.edextweb;

import Controladores.ControladorInstituto;
import Controladores.ControladorUsuario;
import Logica.Usuario;
import Logica.Estudiante;
import Logica.Docente;
import Logica.Instituto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;
    private ControladorInstituto controladorInstituto;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
        controladorInstituto = new ControladorInstituto();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        cargarInstitutos(request);

        request.getRequestDispatcher(
                "/componentes/registrarse.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");

        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        String fechaNacimiento = request.getParameter("fechaNacimiento");

        String instituto = request.getParameter("instituto");

        boolean docente =
                request.getParameter("docente") != null;

        // Validaciones

        if (nickname == null || nickname.trim().isEmpty()) {
            request.setAttribute(
                    "errorNickname",
                    "Ingrese un nickname."
            );
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            request.setAttribute(
                    "errorNombre",
                    "Ingrese su nombre."
            );
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            request.setAttribute(
                    "errorApellido",
                    "Ingrese su apellido."
            );
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute(
                    "errorEmail",
                    "Ingrese su correo electrónico."
            );
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute(
                    "errorPassword",
                    "Ingrese una contraseña."
            );
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {

            request.setAttribute(
                    "errorConfirmPassword",
                    "Confirme su contraseña."
            );

        } else if (password != null
                && !password.equals(confirmPassword)) {

            request.setAttribute(
                    "errorConfirmPassword",
                    "Las contraseñas no coinciden."
            );
        }

        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            request.setAttribute(
                    "errorFechaNacimiento",
                    "Ingrese su fecha de nacimiento."
            );
        }

        if (docente
                && (instituto == null || instituto.trim().isEmpty())) {

            request.setAttribute(
                    "errorInstituto",
                    "Seleccione un instituto."
            );
        }

        // Si hay errores, volvemos al formulario

        if (request.getAttribute("errorNickname") != null
                || request.getAttribute("errorNombre") != null
                || request.getAttribute("errorApellido") != null
                || request.getAttribute("errorEmail") != null
                || request.getAttribute("errorPassword") != null
                || request.getAttribute("errorConfirmPassword") != null
                || request.getAttribute("errorFechaNacimiento") != null
                || request.getAttribute("errorInstituto") != null) {

            cargarInstitutos(request);

            request.getRequestDispatcher(
                    "/componentes/registrarse.jsp"
            ).forward(request, response);

            return;
        }

        // Validar fecha

        LocalDate fecha;

        try {

            fecha = LocalDate.parse(fechaNacimiento);

            if (fecha.isAfter(LocalDate.now())) {

                request.setAttribute(
                        "errorFechaNacimiento",
                        "La fecha no puede ser posterior a hoy."
                );

                cargarInstitutos(request);

                request.getRequestDispatcher(
                        "/componentes/registro.jsp"
                ).forward(request, response);

                return;
            }

        } catch (Exception e) {

            request.setAttribute(
                    "errorFechaNacimiento",
                    "La fecha de nacimiento no es válida."
            );

            cargarInstitutos(request);

            request.getRequestDispatcher(
                    "/componentes/registrarse.jsp"
            ).forward(request, response);

            return;
        }

        // Crear usuario

        Usuario usuario;

        if (docente) {

            usuario = new Docente(
                    nickname.trim(),
                    email.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    fecha
            );

        } else {

            usuario = new Estudiante(
                    nickname.trim(),
                    email.trim(),
                    nombre.trim(),
                    apellido.trim(),
                    fecha
            );
        }

        usuario.setPassword(password);

        // Alta del usuario

        boolean resultado = controladorUsuario.altaUsuario(
                usuario,
                docente ? instituto : null
        );

        if (resultado) {

        HttpSession session = request.getSession();

        session.setAttribute("usuario", usuario);

        response.sendRedirect(
                request.getContextPath()
                + "/componentes/principal.jsp"
        );

        } else {

            request.setAttribute(
                    "errorNickname",
                    "El nickname o correo electrónico ya está en uso."
            );

            cargarInstitutos(request);

            request.getRequestDispatcher(
                    "/componentes/registrarse.jsp"
            ).forward(request, response);
        }
    }

    private void cargarInstitutos(HttpServletRequest request) {

        Map<String, Instituto> institutos =
                controladorInstituto.listarInstitutos();

        request.setAttribute(
                "institutos",
                institutos.values()
        );
    }
}

