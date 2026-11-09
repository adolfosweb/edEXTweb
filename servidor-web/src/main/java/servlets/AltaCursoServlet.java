package servlets;

import WebServices.ClienteSOAP;
import com.mycompany.edextweb.soap.CursoAltaFormularioDTO;
import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
import com.mycompany.edextweb.soap.UsuarioDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "AltaCurso", urlPatterns = {"/AltaCurso"})
public class AltaCursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        UsuarioDTO docente = obtenerDocente(request, response);
        if (docente == null) {
            return;
        }
        boolean modoEdicion = "editar".equals(request.getParameter("modo"));
        String nombre = request.getParameter("nombre");
        try {
            CursoAltaFormularioDTO formulario = ClienteSOAP.obtenerServicio()
                    .obtenerFormularioAltaCurso(
                            docente.getNickname(),
                            nombre,
                            modoEdicion);

            if (formulario == null || !formulario.isAutorizado()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No tienes permiso para realizar esta operación.");
                return;
            }
            if (modoEdicion && !formulario.isCursoEncontrado()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el curso.");
                return;
            }
            request.setAttribute("cursoFormulario", formulario);
            request.setAttribute("modoEdicion", modoEdicion);
            request.setAttribute("mostrarExito","1".equals(request.getParameter("creado")));
            request.setAttribute("fechaMinima", java.time.LocalDate.now().toString());
            
            request.getRequestDispatcher("/componentes/cursos/altaCurso.jsp").forward(request, response);
        } catch (WebServiceException e) {
            getServletContext().log("Error al cargar el formulario de alta de curso.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servidor central.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        UsuarioDTO docente = obtenerDocente(request, response);
        if (docente == null) {
            return;
        }

        String nombre = limpiar(request.getParameter("nombre"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String duracion = limpiar(request.getParameter("duracion"));
        String url = limpiar(request.getParameter("url"));
        String instituto = limpiar(request.getParameter("instituto"));
        String fechaRegistro = limpiar(request.getParameter("fechaRegistro"));

        boolean modoEdicion = "editar".equals(request.getParameter("modo"));
        
        int cantHoras = 0;
        int cantCreditos = 0;
        String error = null;

        try {
            cantHoras = Integer.parseInt(request.getParameter("cantHoras"));
            cantCreditos = Integer.parseInt(request.getParameter("cantCreditos"));
        } catch (NumberFormatException e) {
            error = "Horas y créditos deben ser números enteros.";
        }

        String[] previasArray = request.getParameterValues("previas");
        String[] categoriasArray = request.getParameterValues("categorias");

        List<String> previas = previasArray == null 
                ? new ArrayList<>() : new ArrayList<>(Arrays.asList(previasArray));

        List<String> categorias = categoriasArray == null 
                ? new ArrayList<>() : new ArrayList<>(Arrays.asList(categoriasArray));

        ResultadoOperacionDTO resultado = null;
        if (error == null) {
            try {
                resultado = ClienteSOAP.obtenerServicio().guardarCurso(
                        docente.getNickname(),
                        nombre,
                        descripcion,
                        duracion,
                        cantHoras,
                        cantCreditos,
                        url,
                        instituto,
                        fechaRegistro,
                        modoEdicion,
                        previas,
                        categorias);

                if (resultado == null || !resultado.isExitoso()) {
                    error = resultado == null
                            ? "El servidor central no devolvió un resultado." : resultado.getMensaje();
                }

            } catch (WebServiceException e) {
                getServletContext().log("Error al guardar el curso mediante SOAP.", e);
                error = "No se pudo conectar con el servidor central.";
            }
        }

        if (error == null) {
            if (modoEdicion) {
                response.sendRedirect(request.getContextPath() + "/SvltListarCursos");
            } else {
                response.sendRedirect(request.getContextPath() + "/AltaCurso?creado=1");
            }
            return;
        }
        // Se vuelve a cargar la información de referencia del formulario.
        try {
            CursoAltaFormularioDTO formulario = ClienteSOAP.obtenerServicio()
                    .obtenerFormularioAltaCurso(
                            docente.getNickname(),
                            nombre,
                            modoEdicion);

            if (formulario == null || !formulario.isAutorizado()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No tienes permiso para realizar esta operación.");
                return;
            }

            if (modoEdicion && !formulario.isCursoEncontrado()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el curso.");
                return;
            }

            // Se conservan los datos ingresados por el usuario.
            formulario.setNombre(nombre);
            formulario.setDescripcion(descripcion);
            formulario.setDuracion(duracion);
            formulario.setCantHoras(cantHoras);
            formulario.setCantCreditos(cantCreditos);
            formulario.setUrl(url);
            formulario.setInstituto(instituto);

            if (!modoEdicion) {
                formulario.setFechaRegistro(fechaRegistro);
            }

            CursoAltaFormularioDTO.PreviasSeleccionadas previasDTO = new CursoAltaFormularioDTO.PreviasSeleccionadas();
            previasDTO.getPrevia().addAll(previas);
            formulario.setPreviasSeleccionadas(previasDTO);

            CursoAltaFormularioDTO.CategoriasSeleccionadas categoriasDTO = new CursoAltaFormularioDTO.CategoriasSeleccionadas();
            categoriasDTO.getCategoriaSeleccionada().addAll(categorias);
            formulario.setCategoriasSeleccionadas(categoriasDTO);

            request.setAttribute("cursoFormulario", formulario);
            request.setAttribute("modoEdicion", modoEdicion);
            request.setAttribute("error", error);
            request.setAttribute("fechaMinima", java.time.LocalDate.now().toString());

            if (error.equals("Ya existe un curso con ese nombre.")) {
                request.setAttribute("cursoDuplicado", true);
            }

            request.getRequestDispatcher("/componentes/cursos/altaCurso.jsp").forward(request, response);

        } catch (WebServiceException e) {
            getServletContext().log("Error al recuperar el formulario después de un fallo.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servidor central.");
        }
    }

    private UsuarioDTO obtenerDocente(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("usuario") instanceof UsuarioDTO)) {

            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Solo un docente puede dar de alta cursos.");
            }
            return null;
        }
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (!"DOCENTE".equals(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Solo un docente puede dar de alta cursos.");
            return null;
        }
        return usuario;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}