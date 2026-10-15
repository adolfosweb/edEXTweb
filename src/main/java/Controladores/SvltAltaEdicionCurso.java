package Controladores;

import Logica.Curso;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.Instituto;
import Logica.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "SvltAltaEdicionCurso", urlPatterns = {"/SvltAltaEdicionCurso"})
public class SvltAltaEdicionCurso extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Docente docenteSesion = obtenerDocente(request, response);
        if (docenteSesion == null) {
            return;
        }

        String nombreCurso = limpiar(request.getParameter("curso"));
        Curso curso = buscarCurso(nombreCurso);
        if (curso == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el curso.");
            return;
        }
        if (!docentePuedeGestionarCurso(docenteSesion, curso)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No puedes crear ediciones para cursos de este instituto.");
            return;
        }

        boolean modoEdicion = "editar".equals(request.getParameter("modo"));
        EdicionCurso edicion = null;
        if (modoEdicion) {
            edicion = buscarEdicion(limpiar(request.getParameter("edicion")));
            if (edicion == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró la edición.");
                return;
            }
            if (edicion.getCurso() == null || !nombreCurso.equals(edicion.getCurso().getNombre())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "La edición no pertenece al curso seleccionado.");
                return;
            }
        }

        prepararFormulario(request, curso, docenteSesion, edicion, modoEdicion, null);
        request.getRequestDispatcher("/componentes/cursos/altaEdicionCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Docente docenteSesion = obtenerDocente(request, response);
        if (docenteSesion == null) {
            return;
        }

        String nombreCurso = limpiar(request.getParameter("curso"));
        String nombreEdicion = limpiar(request.getParameter("nombre"));
        String inicioTexto = limpiar(request.getParameter("fechaInicio"));
        String finTexto = limpiar(request.getParameter("fechaFin"));
        String cupoTexto = limpiar(request.getParameter("cupo"));
        boolean modoEdicion = "editar".equals(request.getParameter("modo"));

        Curso curso = buscarCurso(nombreCurso);
        if (curso == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el curso.");
            return;
        }
        if (!docentePuedeGestionarCurso(docenteSesion, curso)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No puedes crear ediciones para cursos de este instituto.");
            return;
        }

        EdicionCurso edicionExistente = null;
        if (modoEdicion) {
            edicionExistente = buscarEdicion(limpiar(request.getParameter("edicionOriginal")));
            if (edicionExistente == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró la edición.");
                return;
            }
            if (edicionExistente.getCurso() == null
                    || !nombreCurso.equals(edicionExistente.getCurso().getNombre())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "La edición no pertenece al curso seleccionado.");
                return;
            }
            if (!edicionExistente.getNombre().equals(nombreEdicion)) {
                request.setAttribute("error", "El nombre de una edición existente no se puede cambiar.");
            }
        }

        String error = (String) request.getAttribute("error");
        if (error == null && (nombreEdicion == null || nombreEdicion.isEmpty()
                || inicioTexto == null || inicioTexto.isEmpty()
                || finTexto == null || finTexto.isEmpty())) {
            error = "Completa el nombre y las fechas de inicio y fin.";
        }

        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;
        if (error == null) {
            try {
                fechaInicio = LocalDate.parse(inicioTexto);
                fechaFin = LocalDate.parse(finTexto);
                if (!modoEdicion && fechaInicio.isBefore(LocalDate.now())) {
                    error = "La fecha de inicio no puede ser anterior a hoy.";
                } else if (!fechaFin.isAfter(fechaInicio)) {
                    error = "La fecha de fin debe ser posterior a la fecha de inicio.";
                }
            } catch (DateTimeParseException ex) {
                error = "Ingresa fechas válidas.";
            }
        }

        int cupo = 0;
        if (error == null && cupoTexto != null && !cupoTexto.isEmpty()) {
            try {
                cupo = Integer.parseInt(cupoTexto);
                if (cupo < 0) {
                    error = "El cupo debe ser cero o un número positivo.";
                }
            } catch (NumberFormatException ex) {
                error = "El cupo debe ser un número entero.";
            }
        }

        Set<String> nombresDocentes = new LinkedHashSet<>();
        String[] docentesEnFormulario = request.getParameterValues("docentes");
        if (docentesEnFormulario != null) {
            for (String nickname : docentesEnFormulario) {
                if (nickname != null && !nickname.trim().isEmpty()) {
                    nombresDocentes.add(nickname.trim());
                }
            }
        }
        if (error == null && nombresDocentes.isEmpty()) {
            error = "Selecciona al menos un docente participante.";
        }

        Map<String, Usuario> usuarios = new ControladorUsuario().listarUsuarios();
        List<Docente> docentesSeleccionados = new ArrayList<>();
        if (error == null) {
            for (String nickname : nombresDocentes) {
                Usuario usuario = usuarios.get(nickname);
                if (!(usuario instanceof Docente)) {
                    error = "Uno de los participantes seleccionados no es un docente válido.";
                    break;
                }
                docentesSeleccionados.add((Docente) usuario);
            }
        }

        EdicionCurso edicionConNombre = error == null
                ? buscarEdicion(nombreEdicion) : null;
        if (error == null && edicionConNombre != null
                && (!modoEdicion || !edicionConNombre.getNombre().equals(edicionExistente.getNombre()))) {
            error = "Ya existe una edición con ese nombre.";
            boolean mismaCurso = edicionConNombre.getCurso() != null
                    && nombreCurso.equals(edicionConNombre.getCurso().getNombre());
            request.setAttribute("edicionDuplicada", mismaCurso);
            request.setAttribute("nombreEdicionDuplicada", edicionConNombre.getNombre());
        }

        if (error != null) {
            request.setAttribute("error", error);
            prepararFormulario(request, curso, docenteSesion, null, modoEdicion, nombresDocentes);
            request.setAttribute("nombreEdicion", nombreEdicion);
            request.setAttribute("fechaInicio", inicioTexto);
            request.setAttribute("fechaFin", finTexto);
            request.setAttribute("cupo", cupoTexto);
            request.setAttribute("edicionOriginal",
                    modoEdicion ? request.getParameter("edicionOriginal") : null);
            request.setAttribute("edicionDuplicada",
                    Boolean.TRUE.equals(request.getAttribute("edicionDuplicada")));
            request.getRequestDispatcher("/componentes/cursos/altaEdicionCurso.jsp").forward(request, response);
            return;
        }

        boolean guardada;
        if (modoEdicion) {
            guardada = modificarEdicion(edicionExistente.getNombre(), fechaInicio, fechaFin, cupo,
                    docentesSeleccionados);
        } else {
            EdicionCurso nuevaEdicion = new EdicionCurso(nombreEdicion, fechaInicio, fechaFin, cupo,
                    LocalDate.now(), curso, docentesSeleccionados.get(0));
            for (int i = 1; i < docentesSeleccionados.size(); i++) {
                nuevaEdicion.getDocentes().add(docentesSeleccionados.get(i));
            }
            guardada = new ControladorCurso().altaEdicion(nuevaEdicion);
        }

        if (!guardada) {
            request.setAttribute("error", "No se pudo guardar la edición. Revisa los datos e inténtalo nuevamente.");
            prepararFormulario(request, curso, docenteSesion, null, modoEdicion, nombresDocentes);
            request.setAttribute("nombreEdicion", nombreEdicion);
            request.setAttribute("fechaInicio", inicioTexto);
            request.setAttribute("fechaFin", finTexto);
            request.setAttribute("cupo", cupoTexto);
            request.setAttribute("edicionOriginal",
                    modoEdicion ? request.getParameter("edicionOriginal") : null);
            request.getRequestDispatcher("/componentes/cursos/altaEdicionCurso.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/SvltConsultarCurso?nombre="
                + java.net.URLEncoder.encode(nombreCurso, "UTF-8"));
    }

    private Docente obtenerDocente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || !(sesion.getAttribute("usuario") instanceof Usuario usuarioSesion)) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return null;
        }
        Usuario usuario = new ControladorUsuario().buscarUsuario(usuarioSesion.getNickname());
        if (!(usuario instanceof Docente docente)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Solo un docente puede gestionar ediciones de cursos.");
            return null;
        }
        return docente;
    }

    private Curso buscarCurso(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return null;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<Curso> cursos = em.createQuery(
                    "SELECT c FROM Curso c LEFT JOIN FETCH c.instituto WHERE c.nombre = :nombre",
                    Curso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            return cursos.isEmpty() ? null : cursos.get(0);
        } finally {
            em.close();
        }
    }

    private EdicionCurso buscarEdicion(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return null;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<EdicionCurso> ediciones = em.createQuery(
                    "SELECT DISTINCT e FROM EdicionCurso e "
                    + "LEFT JOIN FETCH e.curso "
                    + "LEFT JOIN FETCH e.docentes "
                    + "WHERE e.nombre = :nombre",
                    EdicionCurso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            return ediciones.isEmpty() ? null : ediciones.get(0);
        } finally {
            em.close();
        }
    }

    private boolean modificarEdicion(String nombre, LocalDate inicio, LocalDate fin, int cupo,
            List<Docente> docentesSeleccionados) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            EdicionCurso edicion = em.createQuery(
                    "SELECT DISTINCT e FROM EdicionCurso e "
                    + "LEFT JOIN FETCH e.curso "
                    + "LEFT JOIN FETCH e.docentes "
                    + "WHERE e.nombre = :nombre",
                    EdicionCurso.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
            edicion.setFechaInicio(inicio);
            edicion.setFechaFin(fin);
            edicion.setCupo(cupo);
            edicion.getDocentes().clear();
            for (Docente docente : docentesSeleccionados) {
                Docente docenteGestionado = em.find(Docente.class, docente.getNickname());
                if (docenteGestionado == null) {
                    tx.rollback();
                    return false;
                }
                edicion.getDocentes().add(docenteGestionado);
            }
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            getServletContext().log("No se pudo modificar la edición de curso " + nombre, ex);
            return false;
        } finally {
            em.close();
        }
    }

    private boolean docentePuedeGestionarCurso(Docente docente, Curso curso) {
        if (curso.getInstituto() == null) {
            return false;
        }
        String nombreInstitutoCurso = curso.getInstituto().getNombre();
        for (Instituto instituto : docente.getInstitutos()) {
            if (nombreInstitutoCurso.equals(instituto.getNombre())) {
                return true;
            }
        }
        return false;
    }

    private void prepararFormulario(HttpServletRequest request, Curso curso, Docente docente,
            EdicionCurso edicion, boolean modoEdicion, Set<String> docentesSeleccionados) {
        Map<String, Usuario> usuarios = new ControladorUsuario().listarUsuarios();
        List<String> docentes = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            if (usuario instanceof Docente) {
                docentes.add(usuario.getNickname());
            }
        }

        if (docentesSeleccionados == null) {
            docentesSeleccionados = new LinkedHashSet<>();
            if (edicion != null) {
                for (Docente participante : edicion.getDocentes()) {
                    docentesSeleccionados.add(participante.getNickname());
                }
            } else {
                docentesSeleccionados.add(docente.getNickname());
            }
        }

        request.setAttribute("curso", curso.getNombre());
        request.setAttribute("docentes", docentes);
        request.setAttribute("docentesSeleccionados", docentesSeleccionados);
        request.setAttribute("modoEdicion", modoEdicion);
        if (edicion != null) {
            request.setAttribute("nombreEdicion", edicion.getNombre());
            request.setAttribute("fechaInicio", edicion.getFechaInicio());
            request.setAttribute("fechaFin", edicion.getFechaFin());
            request.setAttribute("cupo", edicion.getCupo() == 0 ? "" : edicion.getCupo());
            request.setAttribute("edicionOriginal", edicion.getNombre());
        }
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
