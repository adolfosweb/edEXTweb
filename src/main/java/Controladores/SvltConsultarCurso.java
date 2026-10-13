package Controladores;

import Datatypes.DtCurso;
import Logica.Curso;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SvltConsultarCurso", urlPatterns = {"/SvltConsultarCurso"})
public class SvltConsultarCurso extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el nombre del curso.");
            return;
        }

        Curso curso = new ControladorCurso().buscarCurso(nombre);
        if (curso == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el curso solicitado.");
            return;
        }

        DtCurso dtCurso = new DtCurso();
        dtCurso.setNombre(curso.getNombre());
        dtCurso.setDescripcion(curso.getDescripcion());
        dtCurso.setDuracion(curso.getDuracion());
        dtCurso.setCantHoras(curso.getCantHoras());
        dtCurso.setCantCreditos(curso.getCantCreditos());
        dtCurso.setFecRegistro(curso.getFecRegistro());
        dtCurso.setUrl(curso.getUrl());

        List<String> nombresPrevias = new ArrayList<>();
        for (Curso previa : curso.getPrevias()) {
            nombresPrevias.add(previa.getNombre());
        }
        dtCurso.setPrevias(nombresPrevias);

        request.setAttribute("curso", dtCurso);
        request.getRequestDispatcher("/componentes/cursos/consultarCurso.jsp").forward(request, response);
    }
}
