package Controladores;

import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(name = "SvltModificarPerfil", urlPatterns = {"/ModificarPerfil"})
public class SvltModificarPerfil extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = obtenerUsuarioAutenticado(request, response);
        if (usuario == null) {
            return;
        }

        request.setAttribute("usuarioPerfil", usuario);
        request.setAttribute("actualizado", "1".equals(request.getParameter("actualizado")));
        request.getRequestDispatcher("/componentes/modificarPerfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Usuario usuarioSesion = obtenerUsuarioAutenticado(request, response);
        if (usuarioSesion == null) {
            return;
        }

        IControladorUsuario controladorUsuario = new ControladorUsuario();
        Usuario usuario = controladorUsuario.buscarUsuario(usuarioSesion.getNickname());
        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "La cuenta ya no está disponible.");
            return;
        }

        String nombre = limpiar(request.getParameter("nombre"));
        String apellido = limpiar(request.getParameter("apellido"));
        String fechaNacimiento = limpiar(request.getParameter("fecNac"));
        String nuevaPassword = request.getParameter("password");
        String confirmarPassword = request.getParameter("confirmarPassword");
        String error = null;

        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty()
                || fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            error = "Completa nombre, apellido y fecha de nacimiento.";
        }

        LocalDate fecha = null;
        if (error == null) {
            try {
                fecha = LocalDate.parse(fechaNacimiento);
                if (fecha.isAfter(LocalDate.now())) {
                    error = "La fecha de nacimiento no puede estar en el futuro.";
                }
            } catch (DateTimeParseException ex) {
                error = "La fecha de nacimiento no tiene un formato válido.";
            }
        }

        if (error == null && nuevaPassword != null && !nuevaPassword.isEmpty()
                && !nuevaPassword.equals(confirmarPassword)) {
            error = "La nueva contraseña y su confirmación no coinciden.";
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        if (fecha != null) {
            usuario.setFecNac(fecha);
        }
        if (nuevaPassword != null && !nuevaPassword.isEmpty()) {
            usuario.setPassword(nuevaPassword);
        }

        if (error == null) {
            controladorUsuario.modificarUsuario(usuario);
            Usuario usuarioActualizado = controladorUsuario.buscarUsuario(usuario.getNickname());
            boolean seActualizo = usuarioActualizado != null
                    && nombre.equals(usuarioActualizado.getNombre())
                    && apellido.equals(usuarioActualizado.getApellido())
                    && fecha.equals(usuarioActualizado.getFecNac())
                    && (nuevaPassword == null || nuevaPassword.isEmpty()
                    || nuevaPassword.equals(usuarioActualizado.getPassword()));
            if (seActualizo) {
                request.getSession().setAttribute("usuario", usuarioActualizado);
                response.sendRedirect(request.getContextPath() + "/ModificarPerfil?actualizado=1");
                return;
            }
            error = "No se pudieron guardar los cambios del perfil. Inténtalo nuevamente.";
            usuario = usuarioActualizado == null ? usuario : usuarioActualizado;
        }

        request.setAttribute("error", error);
        request.setAttribute("usuarioPerfil", usuario);
        request.getRequestDispatcher("/componentes/modificarPerfil.jsp").forward(request, response);
    }

    private Usuario obtenerUsuarioAutenticado(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("usuario") instanceof Usuario usuario)) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return null;
        }
        return usuario;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
