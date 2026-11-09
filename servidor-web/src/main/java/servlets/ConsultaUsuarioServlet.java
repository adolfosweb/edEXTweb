package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.UsuarioDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "ConsultaUsuarioServlet",urlPatterns = {"/ConsultaUsuarioServlet"})
public class ConsultaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        try {
            List<UsuarioDTO> usuarios = ClienteSOAP.obtenerServicio().listarUsuariosConsulta();
            usuarios.sort(
                java.util.Comparator.comparing(
                        UsuarioDTO::getApellido,
                        java.util.Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    ).thenComparing(
                        UsuarioDTO::getNombre,
                        java.util.Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
                    ));
            request.setAttribute("usuarios", usuarios);
        } catch (WebServiceException e) {
            request.setAttribute("usuarios", Collections.emptyList());
            request.setAttribute("mensajeError",
                    "No se pudo obtener la lista de usuarios. Intente nuevamente más tarde.");
        }
        request.getRequestDispatcher("/componentes/consultaUsuario.jsp").forward(request, response);
    }
}