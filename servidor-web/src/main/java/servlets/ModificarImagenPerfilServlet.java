
package com.mycompany.edextweb;

import Logica.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import javax.imageio.ImageIO;

@WebServlet(name = "ModificarImagenPerfilServlet",
            urlPatterns = {"/ModificarImagenPerfilServlet"})
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ModificarImagenPerfilServlet extends HttpServlet {

    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);

        if (sesion == null) {
            return null;
        }

        Object objeto = sesion.getAttribute("usuario");

        if (!(objeto instanceof Usuario)) {
            return null;
        }

        return (Usuario) objeto;
    }

    private String urlPerfil(HttpServletRequest request,
                             String nickname,
                             String parametro) {

        return request.getContextPath()
                + "/PerfilUsuarioServlet?nickname="
                + URLEncoder.encode(nickname, StandardCharsets.UTF_8)
                + "&" + parametro;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioSesion = obtenerUsuarioSesion(request);

        if (usuarioSesion == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Debés iniciar sesión.");
            return;
        }

        String nickname = usuarioSesion.getNickname();
        String nicknameSolicitado = request.getParameter("nickname");

        if (nickname == null
                || nicknameSolicitado == null
                || !nickname.equals(nicknameSolicitado)) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No podés modificar la foto de otro usuario.");
            return;
        }

        request.setAttribute("nickname", nickname);

        request.getRequestDispatcher(
                "/componentes/modificarImagenPerfil.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioSesion = obtenerUsuarioSesion(request);

        if (usuarioSesion == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Debés iniciar sesión.");
            return;
        }

        String nickname = usuarioSesion.getNickname();

        if (nickname == null
                || nickname.isBlank()
                || nickname.contains("/")
                || nickname.contains("\\")
                || nickname.equals(".")
                || nickname.equals("..")) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Nickname no válido.");
            return;
        }

        Path temporal = null;

        try {
            Part foto = request.getPart("foto");

            if (foto == null
                    || foto.getSize() == 0
                    || foto.getSubmittedFileName() == null
                    || foto.getSubmittedFileName().isBlank()) {

                response.sendRedirect(urlPerfil(
                        request, nickname, "errorFoto=1"
                ));
                return;
            }

            String nombreOriginal = Paths.get(
                    foto.getSubmittedFileName()
            ).getFileName().toString();

            int punto = nombreOriginal.lastIndexOf('.');

            if (punto < 0) {
                response.sendRedirect(urlPerfil(
                        request, nickname, "errorFoto=1"
                ));
                return;
            }

            String extension = nombreOriginal
                    .substring(punto)
                    .toLowerCase(Locale.ROOT);

            if (!extension.equals(".jpg")
                    && !extension.equals(".jpeg")
                    && !extension.equals(".png")) {

                response.sendRedirect(urlPerfil(
                        request, nickname, "errorFoto=1"
                ));
                return;
            }

            byte[] contenido;

            try (var entrada = foto.getInputStream()) {
                contenido = entrada.readAllBytes();
            }

            // Comprueba que el archivo realmente se pueda leer como imagen.
            if (ImageIO.read(new ByteArrayInputStream(contenido)) == null) {
                response.sendRedirect(urlPerfil(
                        request, nickname, "errorFoto=1"
                ));
                return;
            }

            String directorio = getServletContext()
                    .getRealPath("/imagenesUsuarios");
            
            if (directorio == null) {
                throw new IOException(
                        "No se pudo resolver la carpeta de imágenes."
                );
            }

            Path carpeta = Paths.get(directorio);
            Files.createDirectories(carpeta);

            // Se escribe primero un archivo temporal.
            temporal = Files.createTempFile(
                    carpeta, "foto-", ".tmp"
            );

            Files.write(temporal, contenido);

            Path destino = carpeta.resolve(nickname + extension);

            // Guarda la nueva foto.
            Files.move(
                    temporal,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );

            temporal = null;

            // Elimina fotos anteriores con otra extensión.
            String[] extensiones = {".jpg", ".jpeg", ".png"};

            for (String ext : extensiones) {
                if (!ext.equals(extension)) {
                    Files.deleteIfExists(
                            carpeta.resolve(nickname + ext)
                    );
                }
            }

            response.sendRedirect(urlPerfil(
                    request, nickname, "fotoActualizada=1"
            ));

        } catch (Exception e) {
            log("Error al actualizar la foto de perfil.", e);

            if (temporal != null) {
                try {
                    Files.deleteIfExists(temporal);
                } catch (IOException errorLimpieza) {
                    log("No se pudo eliminar el archivo temporal.",
                            errorLimpieza);
                }
            }

            response.sendRedirect(urlPerfil(
                    request, nickname, "errorFoto=1"
            ));
        }
    }
}
