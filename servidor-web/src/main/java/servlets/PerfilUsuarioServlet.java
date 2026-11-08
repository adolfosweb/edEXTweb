package com.mycompany.edextweb;

import Controladores.ControladorCurso;
import Controladores.ControladorSeguimiento;
import Controladores.ControladorUsuario;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.EstadoInscripcion;
import Logica.InscripcionCurso;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "PerfilUsuarioServlet",
            urlPatterns = {"/PerfilUsuarioServlet"})
public class PerfilUsuarioServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;
    private ControladorSeguimiento controladorSeguimiento;
    private ControladorCurso controladorCurso;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
        controladorSeguimiento = new ControladorSeguimiento();
        controladorCurso = new ControladorCurso();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String nickname = request.getParameter("nickname");

        if (nickname == null || nickname.trim().isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "No se indicó el usuario."
            );
            return;
        }

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);

        if (usuario == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "No se encontró el usuario."
            );
            return;
        }

        request.setAttribute("usuario", usuario);

        HttpSession session = request.getSession(false);
        boolean esPropioPerfil = false;
        boolean siguiendo = false;
        Usuario usuarioSesion = null;

        if (session != null) {
            Object atributoUsuario = session.getAttribute("usuario");
            if (atributoUsuario instanceof Usuario) {
                usuarioSesion = (Usuario) atributoUsuario;
            }
        }

        if (usuarioSesion != null && usuarioSesion.getNickname() != null) {
            esPropioPerfil = usuarioSesion.getNickname()
                    .equals(usuario.getNickname());

            if (!esPropioPerfil) {
                siguiendo = controladorSeguimiento.estaSiguiendo(
                    usuarioSesion.getNickname(),
                    usuario.getNickname()
                );
            }
        }

        request.setAttribute("esPropioPerfil", esPropioPerfil);
        request.setAttribute("siguiendo", siguiendo);

        // Solo se consultan los aceptados cuando un docente ve su propio perfil.
        Map<String, List<InscripcionCurso>> aceptadosPorEdicion = new HashMap<>();

        if (esPropioPerfil && usuario instanceof Docente) {
            Docente docente = (Docente) usuario;

            for (EdicionCurso edicion : docente.getEdiciones()) {
                if (edicion == null || edicion.getNombre() == null) {
                    continue;
                }

                List<InscripcionCurso> aceptados = new ArrayList<>();
                List<InscripcionCurso> inscripciones =
                        controladorCurso.listarInscripcionesEdicion(edicion.getNombre());

                if (inscripciones != null) {
                    for (InscripcionCurso inscripcion : inscripciones) {
                        if (inscripcion != null
                                && inscripcion.getEstado() == EstadoInscripcion.ACEPTADA) {
                            aceptados.add(inscripcion);
                        }
                    }
                }

                aceptadosPorEdicion.put(edicion.getNombre(), aceptados);
            }
        }

        request.setAttribute("aceptadosPorEdicion", aceptadosPorEdicion);

        request.getRequestDispatcher(
                "/componentes/perfilUsuario.jsp"
        ).forward(request, response);
    }
}
