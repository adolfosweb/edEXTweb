package servlets;

import com.mycompany.edextweb.soap.CursoProgramaDTO;
import com.mycompany.edextweb.soap.ProgramaConsultaDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
import WebServices.ClienteSOAP;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "AgregarCursoAProgramaServlet",urlPatterns = {"/agregarCursoAPrograma"})
public class AgregarCursoAProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        cargarFormulario(request);
        request.getRequestDispatcher("/componentes/agregarCursoAPrograma.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombrePrograma = request.getParameter("programa");
        String nombreCurso = request.getParameter("curso");
        if (nombrePrograma == null || nombrePrograma.isBlank() || nombreCurso == null || nombreCurso.isBlank()) {
            
            request.setAttribute("error","Debe seleccionar un programa y un curso.");        
            cargarFormulario(request);
            request.getRequestDispatcher("/componentes/agregarCursoAPrograma.jsp").forward(request, response);
            return;
        }
        try {
            ResultadoOperacionDTO resultado = ClienteSOAP.obtenerServicio().agregarCursoAPrograma(nombrePrograma,nombreCurso);
            if (resultado != null && resultado.isExitoso()) {
                String programaCodificado = URLEncoder.encode(
                        nombrePrograma,
                        StandardCharsets.UTF_8);
                response.sendRedirect(request.getContextPath()
                        + "/consultarPrograma?programa="
                        + programaCodificado);
                return;
            }

            request.setAttribute("error",
                    resultado != null
                            ? resultado.getMensaje() : "No se recibió una respuesta válida del servidor central.");

        } catch (WebServiceException e) {
            log("Error al comunicarse con el servidor central al agregar un curso a un programa.", e);
            request.setAttribute("error",
                    "No se pudo comunicar con el servidor central. Intente nuevamente.");
        }
        cargarFormulario(request);
        request.getRequestDispatcher("/componentes/agregarCursoAPrograma.jsp").forward(request, response);
    }

    private void cargarFormulario(HttpServletRequest request) {

        try {
            List<ProgramaConsultaDTO> programas = ClienteSOAP.obtenerServicio().listarProgramasConsulta();
            List<CursoProgramaDTO> cursos = ClienteSOAP.obtenerServicio().listarCursosParaPrograma();
            request.setAttribute("programas", programas);
            request.setAttribute("cursos", cursos);

        } catch (WebServiceException e) {
            log("Error al cargar los programas y cursos para el formulario.", e);

            request.setAttribute("programas", Collections.emptyList());
            request.setAttribute("cursos", Collections.emptyList());
            request.setAttribute("errorCarga",
                    "No se pudieron cargar los programas y cursos. Intente nuevamente más tarde.");
        }
    }
}