package Controladores;

import Datatypes.DtCurso;
import Logica.Curso;
import Logica.Docente;
import Logica.Instituto;
import Logica.Usuario;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AltaCurso", urlPatterns = {"/AltaCurso"})
public class AltaCurso extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Docente docente = obtenerDocente(request, response);
        if (docente == null) {
            return;
        }

        IControladorCurso controladorCurso = new ControladorCurso();
        DtCurso cursoFormulario = new DtCurso();
        boolean modoEdicion = "editar".equals(request.getParameter("modo"));

        if (modoEdicion) {
            String nombre = request.getParameter("nombre");
            Curso existente = controladorCurso.buscarCurso(nombre);
            if (existente == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el curso.");
                return;
            }
            if (existente.getInstituto() == null
                    || !docenteTieneInstituto(docente, existente.getInstituto().getNombre())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No tienes permiso para modificar este curso.");
                return;
            }
            cursoFormulario = crearDtCurso(existente);
            request.setAttribute("previasSeleccionadas", nombresPrevias(existente));
        }

        prepararFormulario(request, docente, controladorCurso, cursoFormulario, modoEdicion);
        request.setAttribute("fechaRegistro", modoEdicion && cursoFormulario.getFecRegistro() != null
                ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(cursoFormulario.getFecRegistro())
                : LocalDate.now().toString());
        request.setAttribute("mostrarExito", "1".equals(request.getParameter("creado")));
        request.getRequestDispatcher("/componentes/cursos/altaCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Docente docente = obtenerDocente(request, response);
        if (docente == null) {
            return;
        }

        IControladorCurso controladorCurso = new ControladorCurso();
        String nombre = limpiar(request.getParameter("nombre"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String duracion = limpiar(request.getParameter("duracion"));
        String url = limpiar(request.getParameter("url"));
        String institutoNombre = limpiar(request.getParameter("instituto"));
        String fechaRegistroTexto = limpiar(request.getParameter("fechaRegistro"));
        boolean modoEdicion = "editar".equals(request.getParameter("modo"));
        String error = validarCampos(nombre, descripcion, duracion, url, institutoNombre);

        LocalDate fechaRegistro = null;
        if (error == null && !modoEdicion) {
            if (fechaRegistroTexto == null || fechaRegistroTexto.isEmpty()) {
                error = "Selecciona la fecha de registro del curso.";
            } else {
                try {
                    fechaRegistro = LocalDate.parse(fechaRegistroTexto);
                    if (fechaRegistro.isBefore(LocalDate.now())) {
                        error = "La fecha de registro no puede ser anterior a hoy.";
                    }
                } catch (DateTimeParseException ex) {
                    error = "Selecciona una fecha de registro válida.";
                }
            }
        }

        if (error == null && !docenteTieneInstituto(docente, institutoNombre)) {
            error = "Selecciona uno de los institutos asociados a tu cuenta docente.";
        }

        int cantHoras = 0;
        int cantCreditos = 0;
        if (error == null) {
            try {
                cantHoras = Integer.parseInt(request.getParameter("cantHoras"));
                cantCreditos = Integer.parseInt(request.getParameter("cantCreditos"));
                if (cantHoras < 0 || cantCreditos < 0) {
                    error = "Horas y créditos no pueden ser negativos.";
                }
            } catch (NumberFormatException ex) {
                error = "Horas y créditos deben ser números enteros.";
            }
        }

        Curso cursoExistente = null;
        if (error == null && modoEdicion) {
            cursoExistente = controladorCurso.buscarCurso(nombre);
            if (cursoExistente == null
                    || cursoExistente.getInstituto() == null
                    || !docenteTieneInstituto(docente, cursoExistente.getInstituto().getNombre())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No tienes permiso para modificar este curso.");
                return;
            }
        } else if (error == null && controladorCurso.buscarCurso(nombre) != null) {
            error = "Ya existe un curso con ese nombre.";
            request.setAttribute("cursoDuplicado", true);
        }

        List<Curso> previas = new ArrayList<>();
        if (error == null) {
            String[] nombresPrevias = request.getParameterValues("previas");
            if (nombresPrevias != null) {
                for (String nombrePrevia : nombresPrevias) {
                    if (nombre.equals(nombrePrevia)) {
                        error = "Un curso no puede ser previa de sí mismo.";
                        break;
                    }
                    Curso previa = controladorCurso.buscarCurso(nombrePrevia);
                    if (previa == null) {
                        error = "Una de las previas seleccionadas ya no existe.";
                        break;
                    }
                    if (!previas.contains(previa)) {
                        previas.add(previa);
                    }
                }
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            DtCurso cursoFormulario = crearDtCurso(nombre, descripcion, duracion, cantHoras, cantCreditos,
                    url, institutoNombre);
            request.setAttribute("previasSeleccionadas", request.getParameterValues("previas"));
            prepararFormulario(request, docente, controladorCurso, cursoFormulario, modoEdicion);
            request.setAttribute("fechaRegistro", modoEdicion && cursoExistente != null
                    && cursoExistente.getFecRegistro() != null
                    ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(cursoExistente.getFecRegistro())
                    : fechaRegistroTexto == null || fechaRegistroTexto.isEmpty()
                            ? LocalDate.now().toString() : fechaRegistroTexto);
            request.setAttribute("cursoDuplicado", Boolean.TRUE.equals(request.getAttribute("cursoDuplicado")));
            request.getRequestDispatcher("/componentes/cursos/altaCurso.jsp").forward(request, response);
            return;
        }

        Curso curso = new Curso(nombre, descripcion, duracion, cantHoras, cantCreditos,
                modoEdicion ? cursoExistente.getFecRegistro()
                        : java.sql.Date.valueOf(fechaRegistro), url);
        curso.setInstituto(new Instituto(institutoNombre));
        curso.setPrevias(previas);

        boolean guardado = modoEdicion
                ? controladorCurso.modificarCurso(curso)
                : controladorCurso.altaCurso(curso);
        if (!guardado) {
            request.setAttribute("error", modoEdicion
                    ? "No se pudo modificar el curso. Revisa los datos e inténtalo nuevamente."
                    : "No se pudo dar de alta el curso. Revisa los datos e inténtalo nuevamente.");
            request.setAttribute("previasSeleccionadas", request.getParameterValues("previas"));
            prepararFormulario(request, docente, controladorCurso,
                    crearDtCurso(nombre, descripcion, duracion, cantHoras, cantCreditos, url, institutoNombre),
                    modoEdicion);
            request.setAttribute("fechaRegistro", modoEdicion && cursoExistente != null
                    && cursoExistente.getFecRegistro() != null
                    ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(cursoExistente.getFecRegistro())
                    : fechaRegistroTexto);
            request.getRequestDispatcher("/componentes/cursos/altaCurso.jsp").forward(request, response);
            return;
        }

        if (modoEdicion) {
            response.sendRedirect(request.getContextPath() + "/SvltListarCursos");
        } else {
            response.sendRedirect(request.getContextPath() + "/AltaCurso?creado=1");
        }
    }

    private Docente obtenerDocente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("usuario") instanceof Docente usuarioSesion)) {
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo un docente puede dar de alta cursos.");
            }
            return null;
        }

        Usuario usuarioActual = new ControladorUsuario().buscarUsuario(usuarioSesion.getNickname());
        if (!(usuarioActual instanceof Docente docente)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo un docente puede dar de alta cursos.");
            return null;
        }
        return docente;
    }

    private void prepararFormulario(HttpServletRequest request, Docente docente,
            IControladorCurso controladorCurso, DtCurso cursoFormulario, boolean modoEdicion) {
        List<String> institutos = new ArrayList<>();
        for (Instituto instituto : docente.getInstitutos()) {
            institutos.add(instituto.getNombre());
        }

        List<String> cursosExistentes = new ArrayList<>();
        Map<String, Curso> cursos = controladorCurso.listarCursos();
        if (cursos != null) {
            for (Curso curso : cursos.values()) {
                if (!modoEdicion || !curso.getNombre().equals(cursoFormulario.getNombre())) {
                    cursosExistentes.add(curso.getNombre());
                }
            }
        }

        request.setAttribute("institutos", institutos);
        request.setAttribute("cursosExistentes", cursosExistentes);
        request.setAttribute("cursoFormulario", cursoFormulario);
        request.setAttribute("modoEdicion", modoEdicion);
    }

    private boolean docenteTieneInstituto(Docente docente, String nombreInstituto) {
        if (nombreInstituto == null) {
            return false;
        }
        for (Instituto instituto : docente.getInstitutos()) {
            if (nombreInstituto.equals(instituto.getNombre())) {
                return true;
            }
        }
        return false;
    }

    private List<String> nombresPrevias(Curso curso) {
        List<String> nombres = new ArrayList<>();
        for (Curso previa : curso.getPrevias()) {
            nombres.add(previa.getNombre());
        }
        return nombres;
    }

    private DtCurso crearDtCurso(Curso curso) {
        DtCurso dtCurso = crearDtCurso(curso.getNombre(), curso.getDescripcion(), curso.getDuracion(),
                curso.getCantHoras(), curso.getCantCreditos(), curso.getUrl(),
                curso.getInstituto() == null ? null : curso.getInstituto().getNombre());
        dtCurso.setFecRegistro(curso.getFecRegistro());
        return dtCurso;
    }

    private DtCurso crearDtCurso(String nombre, String descripcion, String duracion, int cantHoras,
            int cantCreditos, String url, String instituto) {
        DtCurso dtCurso = new DtCurso();
        dtCurso.setNombre(nombre);
        dtCurso.setDescripcion(descripcion);
        dtCurso.setDuracion(duracion);
        dtCurso.setCantHoras(cantHoras);
        dtCurso.setCantCreditos(cantCreditos);
        dtCurso.setUrl(url);
        dtCurso.setInstituto(instituto);
        return dtCurso;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String validarCampos(String nombre, String descripcion, String duracion, String url,
            String instituto) {
        if (nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty()
                || duracion == null || duracion.isEmpty() || url == null || url.isEmpty()
                || instituto == null || instituto.isEmpty()) {
            return "Completa todos los campos obligatorios.";
        }
        return null;
    }
}
