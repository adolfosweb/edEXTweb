package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.UsuarioDTO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class RecordarmeFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(RecordarmeFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {
        // No necesita inicializar controladores locales.
    }

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Si ya hay un usuario en sesión, no hace falta recuperarlo.
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("recordarme".equals(cookie.getName())
                            && cookie.getValue() != null
                            && !cookie.getValue().isBlank()) {

                        String hash = UtilToken.calcularHash(cookie.getValue());
                        try {
                            EdextWebService servicio = ClienteSOAP.obtenerServicio();
                            // Buscar si existe un token válido.
                            String nickname = servicio.buscarNicknameTokenValido(hash);
                            if (nickname != null && !nickname.isBlank()) {
                                
                                // Recuperar los datos del usuario.
                                UsuarioDTO usuario = servicio.buscarUsuarioPorNickname(nickname);
                                if (usuario != null) {
                                    
                                    HttpSession nuevaSesion = req.getSession(true);
                                    nuevaSesion.setAttribute("usuario",usuario);

                                } else {
                                    // El usuario ya no existe.
                                    eliminarCookie(req, res);
                                    servicio.eliminarTokenRecordarme(hash);
                                }

                            } else {
                                // El token ya no es válido.
                                eliminarCookie(req, res);
                                servicio.eliminarTokenRecordarme(hash);
                            }
                        } catch (WebServiceException e) {
                            // Si el servidor central no está disponible,
                            // no eliminamos la cookie ni el token.
                            LOGGER.log(Level.WARNING,
                                    "No se pudo recuperar la sesión mediante SOAP.",e);
                        }
                        break;
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    private void eliminarCookie(HttpServletRequest request,HttpServletResponse response) {
        Cookie cookie = new Cookie("recordarme", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
    }
}
