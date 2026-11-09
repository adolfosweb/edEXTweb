package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ListadoCursosDTO;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.util.Collections;

@WebServlet(name = "SvltListarCursos", urlPatterns = {"/SvltListarCursos"})
public class ListarCursosServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        try {
            String nickname = null;
            HttpSession sesion = request.getSession(false);
            if (sesion != null) {
                Object usuario = sesion.getAttribute("usuario");

                if (usuario instanceof UsuarioDTO usuarioDTO) {
                    nickname = usuarioDTO.getNickname();
                }
            }

            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            ListadoCursosDTO listado = servicio.listarCursosParaWeb(nickname);
            if (listado != null) {
                request.setAttribute("cursos", listado.getCursos());
                request.setAttribute("cursosEditables", listado.getCursosEditables());
            } else {
                request.setAttribute("cursos", Collections.emptyList());
                request.setAttribute("cursosEditables", Collections.emptyList());
            }
        } catch (WebServiceException e) {
            request.setAttribute("cursos", Collections.emptyList());
            request.setAttribute("cursosEditables", Collections.emptyList());
            request.setAttribute("mensajeError","No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        request.getRequestDispatcher("/componentes/cursos/listarCursos.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para listar cursos";
    }
}