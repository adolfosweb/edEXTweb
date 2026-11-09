package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ProgramaConsultaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "ConsultarProgramaServlet", urlPatterns = {"/consultarPrograma"})
public class ConsultarProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();

            // Obtener todos los programas desde el servidor central.
            List<ProgramaConsultaDTO> programas = servicio.listarProgramasConsulta();

            if (programas == null) {
                programas = Collections.emptyList();
            }

            // Determinar qué programa se debe mostrar.
            String nombreSeleccionado = request.getParameter("programa");
            ProgramaConsultaDTO programaSeleccionado = null;

            if (nombreSeleccionado != null && !nombreSeleccionado.isBlank()) {
                for (ProgramaConsultaDTO programa : programas) {
                    if (programa.getNombre().equals(nombreSeleccionado)) {
                        programaSeleccionado = programa;
                        break;
                    }
                }
            } else if (!programas.isEmpty()) {
                programaSeleccionado = programas.get(0);
            }

            request.setAttribute("programas", programas);
            request.setAttribute("programaSeleccionado", programaSeleccionado);

        } catch (WebServiceException e) {
            request.setAttribute("mensajeError","No se pudo conectar con el servidor central. Intente nuevamente.");
        }

        request.getRequestDispatcher("/componentes/consultarPrograma.jsp").forward(request, response);
    }
}