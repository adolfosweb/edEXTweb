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

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private static final int DURACION_COOKIE = 30 * 24 * 60 * 60;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String identificador = request.getParameter("identificador");
        String password = request.getParameter("password");

        try {
            // Obtener el servicio SOAP.
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            // Autenticar al usuario en el servidor central.
            UsuarioDTO usuario = servicio.iniciarSesion(identificador, password);
            if (usuario != null) {
                // Crear la sesión local con el DTO, no con la entidad JPA.
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                String recordarme = request.getParameter("recordarme");
                if ("true".equals(recordarme)) {
                    // Generar un token secreto.
                    String token = UtilToken.generarToken();
                    String hash = UtilToken.calcularHash(token);
                    // Guardar el hash mediante SOAP.
                    boolean guardado = servicio.guardarTokenRecordarme(hash, usuario.getNickname());

                    if (guardado) {
                        // Enviar el token original al navegador.
                        Cookie cookie = new Cookie("recordarme", token);
                        cookie.setMaxAge(DURACION_COOKIE);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(request.isSecure());
                        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
                        cookie.setAttribute("SameSite", "Lax");

                        response.addCookie(cookie);
                    }
                } else {
                    // Si no se marcó "Recordarme", eliminar una cookie anterior.
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie c : cookies) {
                            if ("recordarme".equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {

                                String hash = UtilToken.calcularHash(c.getValue());
                                servicio.eliminarTokenRecordarme(hash);
                                Cookie eliminar = new Cookie("recordarme", "");
                                eliminar.setMaxAge(0);
                                eliminar.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
                                eliminar.setHttpOnly(true);
                                eliminar.setSecure(request.isSecure());
                                eliminar.setAttribute("SameSite", "Lax");

                                response.addCookie(eliminar);
                                break;
                            }
                        }
                    }
                }
                response.sendRedirect(request.getContextPath() + "/componentes/principal.jsp");
            } else {
                request.setAttribute("error","Usuario o contraseña incorrectos.");
                request.getRequestDispatcher("/componentes/login.jsp").forward(request, response);
            }

        } catch (WebServiceException e) {
            // El servidor central no está disponible o falló la comunicación SOAP.
            request.setAttribute("error","No se pudo conectar con el servidor. Intentá nuevamente.");
            request.getRequestDispatcher("/componentes/login.jsp").forward(request, response);
        }
    }
}
