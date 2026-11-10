package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;

@WebServlet(name = "MovilLoginServlet", urlPatterns = {"/movil/login"})
public class MovilLoginServlet extends HttpServlet {

    private static final int DURACION_COOKIE = 30 * 24 * 60 * 60;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si ya hay sesión (por ejemplo, recuperada por la cookie "recordarme"), se entra directo.
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/movil/componentes/index.jsp");
            return;
        }
        request.getRequestDispatcher("/movil/componentes/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String identificador = request.getParameter("identificador");
        String password = request.getParameter("password");

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            UsuarioDTO usuario = servicio.iniciarSesion(identificador, password);

            if (usuario == null) {
                volverAlLogin(request, response, "Usuario o contraseña incorrectos.");
                return;
            }

            // El móvil es solo para estudiantes.
            String tipo = usuario.getTipoUsuario();
            if (tipo != null && tipo.toLowerCase().contains("docente")) {
                volverAlLogin(request, response, "Solo los estudiantes pueden ingresar desde el móvil.");
                return;
            }

            HttpSession sesion = request.getSession();
            request.changeSessionId();
            sesion.setAttribute("usuario", usuario);

            if ("true".equals(request.getParameter("recordarme"))) {
                String token = UtilToken.generarToken();
                String hash = UtilToken.calcularHash(token);
                if (servicio.guardarTokenRecordarme(hash, usuario.getNickname())) {
                    Cookie cookie = new Cookie("recordarme", token);
                    cookie.setMaxAge(DURACION_COOKIE);
                    cookie.setHttpOnly(true);
                    cookie.setSecure(request.isSecure());
                    cookie.setPath(rutaCookie(request));
                    cookie.setAttribute("SameSite", "Lax");
                    response.addCookie(cookie);
                }
            } else {
                // Sin "recordarme": se elimina una cookie anterior, si había.
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("recordarme".equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                            servicio.eliminarTokenRecordarme(UtilToken.calcularHash(c.getValue()));
                            Cookie eliminar = new Cookie("recordarme", "");
                            eliminar.setMaxAge(0);
                            eliminar.setPath(rutaCookie(request));
                            eliminar.setHttpOnly(true);
                            eliminar.setSecure(request.isSecure());
                            eliminar.setAttribute("SameSite", "Lax");
                            response.addCookie(eliminar);
                            break;
                        }
                    }
                }
            }

            response.sendRedirect(request.getContextPath() + "/movil/componentes/index.jsp");

        } catch (WebServiceException e) {
            volverAlLogin(request, response, "No se pudo conectar con el servidor. Intentá nuevamente.");
        }
    }

    private void volverAlLogin(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/movil/componentes/login.jsp").forward(request, response);
    }

    private String rutaCookie(HttpServletRequest request) {
        return request.getContextPath().isEmpty() ? "/" : request.getContextPath();
    }
}