
package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.InscripcionCursoDTO;
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
import java.util.List;

@WebServlet(name = "VerResultadosEvaluacionesEstudianteServlet",urlPatterns = {"/verResultadosEvaluacionesEstudiante"})
public class VerResultadosEvaluacionesEstudianteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        // Verificar que exista una sesión iniciada.
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return;
        }
        Object usuarioSesion = sesion.getAttribute("usuario");
        // Verificar que el usuario sea un estudiante.
        if(!(usuarioSesion instanceof UsuarioDTO usuario) || !"ESTUDIANTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No tenés permisos para consultar estos resultados.");
            return;
        }
        try { 
            List<InscripcionCursoDTO> inscripciones =
                    ClienteSOAP.obtenerServicio().verResultadosEvaluacionesEstudiante(usuario.getNickname());
            if(inscripciones == null) {
                inscripciones = Collections.emptyList();
            }
            request.setAttribute("inscripciones", inscripciones);
            request.setAttribute("usuario", usuario);
        } catch(WebServiceException e) {
            request.setAttribute("mensajeError","No se pudieron obtener los resultados. Intentá nuevamente más tarde.");
            request.setAttribute("inscripciones",Collections.emptyList());
        }
        request.getRequestDispatcher("/componentes/verResultadosEvaluacionesEstudiante.jsp").forward(request, response);
    }
}
