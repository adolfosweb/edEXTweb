package Controladores;

import Datatypes.DtCurso;
import Logica.Curso;
import Logica.Docente;
import Logica.Instituto;
import Logica.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SvltListarCursos", urlPatterns = {"/SvltListarCursos"})
public class SvltListarCursos extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtenemos el controlador (instanciación directa para este ejemplo)
        IControladorCurso controladorCurso = new ControladorCurso();

        // El controlador existente devuelve Map<String, Curso>
        Map<String, Curso> cursosMap = controladorCurso.listarCursos();
        
        // Convertimos a una lista de DtCurso (Data Types)
        List<DtCurso> listaDtCursos = new ArrayList<>();
        Set<String> cursosEditables = new HashSet<>();
        Usuario usuarioSesion = request.getSession(false) == null
                ? null : (Usuario) request.getSession(false).getAttribute("usuario");
        Docente docente = usuarioSesion instanceof Docente
                ? (Docente) new ControladorUsuario().buscarUsuario(usuarioSesion.getNickname()) : null;
        
        if (cursosMap != null) {
            for (Curso c : cursosMap.values()) {
                DtCurso dt = new DtCurso();
                dt.setNombre(c.getNombre());
                dt.setDescripcion(c.getDescripcion());
                dt.setDuracion(c.getDuracion());
                dt.setCantHoras(c.getCantHoras());
                dt.setCantCreditos(c.getCantCreditos());
                dt.setUrl(c.getUrl());
                if (c.getInstituto() != null) {
                    String nombreInstituto = c.getInstituto().getNombre();
                    dt.setInstituto(nombreInstituto);
                    if (docente != null && docente.getInstitutos().stream()
                            .map(Instituto::getNombre)
                            .anyMatch(nombreInstituto::equals)) {
                        cursosEditables.add(c.getNombre());
                    }
                }
                listaDtCursos.add(dt);
            }
        }

        // Enviamos la lista de Dt a la vista
        request.setAttribute("cursos", listaDtCursos);
        request.setAttribute("cursosEditables", cursosEditables);
        request.getRequestDispatcher("/componentes/cursos/listarCursos.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para listar cursos";
    }
}
