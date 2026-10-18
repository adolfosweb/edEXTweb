package com.mycompany.edextweb;

import Controladores.ControladorPrograma;
import Logica.Estudiante;
import Logica.InscripcionPrograma;
import Logica.ProgramaFormacion;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@WebServlet(name = "InscripcionProgramaServlet", urlPatterns = {"/inscripcionPrograma"})
public class InscripcionProgramaServlet extends HttpServlet {

    private ControladorPrograma controladorPrograma;

    @Override
    public void init() throws ServletException {
        controladorPrograma = new ControladorPrograma();
    }

     // Mostrar los programas disponibles
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        mostrarProgramas(request, response);
    }

    // Procesar una inscripción
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contexto = request.getContextPath();

        // 1. Comprobar la sesión
        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof Usuario)) {
            response.sendRedirect(contexto + "/componentes/login.jsp");
            return;
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // 2. Solo los estudiantes pueden inscribirse
        if (!(usuario instanceof Estudiante)) {
            request.setAttribute("error","Solo los estudiantes pueden inscribirse a programas.");
             mostrarProgramas(request, response);
            return;
        }
        Estudiante estudiante = (Estudiante) usuario;

        // 3. Obtener el programa seleccionado
        String nombrePrograma = request.getParameter("programa");

        if (nombrePrograma == null || nombrePrograma.isBlank()) {
            request.setAttribute("error","No se especificó el programa.");
            mostrarProgramas(request, response);
            return;
        }

        ProgramaFormacion programa = controladorPrograma.buscarPrograma(nombrePrograma);

        if (programa == null) {
            request.setAttribute("error","El programa seleccionado no existe.");
            mostrarProgramas(request, response);
            return;
        }

        // 4. Comprobar si ya existe una inscripción
        InscripcionPrograma existente = controladorPrograma.buscarInscripcionPrograma(estudiante.getNickname(),nombrePrograma);

        if (existente != null) {
            request.setAttribute("error","Ya estás inscrito en este programa.");
            mostrarProgramas(request, response);
            return;
        }

        // 5. Crear y guardar la inscripción
        InscripcionPrograma inscripcion = new InscripcionPrograma(estudiante,LocalDate.now(),programa);

        boolean resultado = controladorPrograma.inscribirEstudiantePrograma(inscripcion);

        if (resultado) {
            request.setAttribute("mensaje","Te inscribiste correctamente al programa " + programa.getNombre() + ".");
        } else {
            request.setAttribute("error","No se pudo completar la inscripción. " + "Intentá nuevamente.");
        }
        mostrarProgramas(request, response);
    }

    private void mostrarProgramas(HttpServletRequest request,  HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, ProgramaFormacion> programas = controladorPrograma.listarProgramas();
        request.setAttribute("programas", programas);
        request.getRequestDispatcher("/componentes/inscripcionPrograma.jsp").forward(request, response);
    }
}
