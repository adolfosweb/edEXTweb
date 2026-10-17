
package com.mycompany.edextweb;

import Controladores.ControladorPrograma;
import Controladores.ControladorUsuario;
import Logica.ProgramaFormacion;
import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/BuscarGeneralServlet")
public class BuscarGeneralServlet extends HttpServlet {

    private ControladorUsuario controladorUsuario;
    private ControladorPrograma controladorPrograma;

    @Override
    public void init() throws ServletException {
        controladorUsuario = new ControladorUsuario();
        controladorPrograma = new ControladorPrograma();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String texto = request.getParameter("q");

        if (texto == null) {
            texto = "";
        }

        texto = texto.trim().toLowerCase();

        List<String> resultados = new ArrayList<>();

        if (!texto.isEmpty()) {

            Map<String, Usuario> usuarios =
                    controladorUsuario.listarUsuarios();

            if (usuarios != null) {
                for (Usuario u : usuarios.values()) {

                    String nombre = u.getNombre() == null
                            ? "" : u.getNombre();
                    String apellido = u.getApellido() == null
                            ? "" : u.getApellido();
                    String nickname = u.getNickname() == null
                            ? "" : u.getNickname();

                    String datos = (nombre + " " + apellido + " "
                            + nickname).toLowerCase();

                    if (datos.contains(texto)) {
                        String url = request.getContextPath()
                                + "/PerfilUsuarioServlet?nickname="
                                + URLEncoder.encode(
                                    nickname,
                                    StandardCharsets.UTF_8
                                );

                        resultados.add(
                            "{\"tipo\":\"Usuario\",\"nombre\":\""
                            + escapar(nombre + " " + apellido)
                            + "\",\"detalle\":\"Nickname: "
                            + escapar(nickname)
                            + "\",\"url\":\"" + escapar(url) + "\"}"
                        );
                    }
                }
            }

            Map<String, ProgramaFormacion> programas =
                    controladorPrograma.listarProgramas();

            if (programas != null) {
                for (ProgramaFormacion p : programas.values()) {

                    String nombre = p.getNombre() == null
                            ? "" : p.getNombre();

                    if (nombre.toLowerCase().contains(texto)) {
                        String url = request.getContextPath()
                                + "/consultarPrograma?programa="
                                + URLEncoder.encode(
                                    nombre,
                                    StandardCharsets.UTF_8
                                );

                        resultados.add(
                            "{\"tipo\":\"Programa\",\"nombre\":\""
                            + escapar(nombre)
                            + "\",\"detalle\":\"Programa de formación"
                            + "\",\"url\":\"" + escapar(url) + "\"}"
                        );
                    }
                }
            }
        }

        response.getWriter().write(
                "{\"resultados\":["
                + String.join(",", resultados)
                + "]}"
        );
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
