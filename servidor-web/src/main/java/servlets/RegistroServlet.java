package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.InstitutoDTO;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegistroServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        cargarInstitutos(request);

        request.getRequestDispatcher("/componentes/registrarse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        String nickname = request.getParameter("nickname");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");

        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String instituto = request.getParameter("instituto");

        boolean docente = request.getParameter("docente") != null;

        // =====================================================
        // VALIDACIONES
        // =====================================================

        if (nickname == null || nickname.trim().isEmpty()) {
            request.setAttribute("errorNickname", "Ingrese un nickname.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            request.setAttribute("errorNombre", "Ingrese su nombre.");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            request.setAttribute("errorApellido", "Ingrese su apellido.");
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorEmail","Ingrese su correo electrónico.");
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("errorPassword","Ingrese una contraseña.");
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("errorConfirmPassword","Confirme su contraseña.");
        } else if (password != null && !password.equals(confirmPassword)) {
            request.setAttribute("errorConfirmPassword","Las contraseñas no coinciden.");
        }

        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            request.setAttribute("errorFechaNacimiento","Ingrese su fecha de nacimiento.");
        }

        if (docente && (instituto == null || instituto.trim().isEmpty())) {
            request.setAttribute("errorInstituto","Seleccione un instituto.");
        }

        // Si hay errores, volvemos al formulario.
        if (hayErrores(request)) {
            cargarInstitutos(request);
            request.getRequestDispatcher("/componentes/registrarse.jsp").forward(request, response);
            return;
        }

        // =====================================================
        // VALIDAR FECHA
        // =====================================================

        LocalDate fecha;

        try {
            fecha = LocalDate.parse(fechaNacimiento);
            if (fecha.isAfter(LocalDate.now())) {
                request.setAttribute("errorFechaNacimiento","La fecha no puede ser posterior a hoy.");

                cargarInstitutos(request);
                request.getRequestDispatcher("/componentes/registrarse.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("errorFechaNacimiento","La fecha de nacimiento no es válida.");
            cargarInstitutos(request);
            request.getRequestDispatcher("/componentes/registrarse.jsp").forward(request, response);
            return;
        }

        // =====================================================
        // REGISTRO MEDIANTE SOAP
        // =====================================================

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            
            UsuarioDTO usuarioDTO = new UsuarioDTO();

            usuarioDTO.setNickname(nickname.trim());
            usuarioDTO.setCorreoElectronico(email.trim());
            usuarioDTO.setNombre(nombre.trim());
            usuarioDTO.setApellido(apellido.trim());
            usuarioDTO.setFecNac(fecha.toString());
            usuarioDTO.setTipoUsuario(docente ? "DOCENTE" : "ESTUDIANTE");

            UsuarioDTO usuarioRegistrado = servicio.registrarUsuario(usuarioDTO,password,docente ? instituto : null);

            if (usuarioRegistrado != null) {

                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuarioRegistrado);
                response.sendRedirect(request.getContextPath() + "/componentes/principal.jsp");

            } else {

                request.setAttribute("errorNickname","El nickname o correo electrónico ya está en uso.");
                cargarInstitutos(request);
                request.getRequestDispatcher("/componentes/registrarse.jsp").forward(request, response);
            }
        } catch (WebServiceException e) {

            LOGGER.log(Level.SEVERE,
                    "Error al comunicarse con el servidor central durante el registro.",e);

            request.setAttribute("errorGeneral","No se pudo conectar con el servidor. Intentá nuevamente.");

            cargarInstitutos(request);
            request.getRequestDispatcher("/componentes/registrarse.jsp").forward(request, response);
        }
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private boolean hayErrores(HttpServletRequest request) {

        return request.getAttribute("errorNickname") != null
                || request.getAttribute("errorNombre") != null
                || request.getAttribute("errorApellido") != null
                || request.getAttribute("errorEmail") != null
                || request.getAttribute("errorPassword") != null
                || request.getAttribute("errorConfirmPassword") != null
                || request.getAttribute("errorFechaNacimiento") != null
                || request.getAttribute("errorInstituto") != null;
    }

    private void cargarInstitutos(HttpServletRequest request) {

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            List<InstitutoDTO> institutos = servicio.listarInstitutos();
            request.setAttribute("institutos", institutos);

        } catch (WebServiceException e) {

            LOGGER.log(Level.SEVERE,"No se pudieron cargar los institutos.",e);

            request.setAttribute("errorGeneral","No se pudieron cargar los institutos. Intentá nuevamente.");
        }
    }
}
