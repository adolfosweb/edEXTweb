package com.mycompany.edextweb;

import Controladores.ControladorPrograma;
import Logica.ProgramaFormacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024,maxRequestSize = 6 * 1024 * 1024)
@WebServlet(name = "CrearProgramaServlet", urlPatterns = {"/crearPrograma"})
public class CrearProgramaServlet extends HttpServlet {

    private ControladorPrograma controladorPrograma;

    @Override
    public void init() throws ServletException {
        controladorPrograma = new ControladorPrograma();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String fechaInicioTexto = request.getParameter("fechaInicio");
        String fechaFinTexto = request.getParameter("fechaFin");

        nombre = (nombre == null) ? "" : nombre.trim();
        descripcion = (descripcion == null) ? "" : descripcion.trim();
        fechaInicioTexto = (fechaInicioTexto == null) ? "" : fechaInicioTexto.trim();
        fechaFinTexto = (fechaFinTexto == null) ? "" : fechaFinTexto.trim();

        boolean valido = true;

        if (nombre.isEmpty()) {
            request.setAttribute("errorNombre", "Ingrese un nombre.");
            valido = false;
        }
        if (fechaInicioTexto.isEmpty()) {
            request.setAttribute("errorFecI", "Ingrese una fecha de inicio.");
            valido = false;
        }
        if (fechaFinTexto.isEmpty()) {
            request.setAttribute("errorFecF", "Ingrese una fecha de fin.");
            valido = false;
        }
        if (!valido) {
            request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
            return;
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;

        try {
            fechaInicio = LocalDate.parse(fechaInicioTexto);
            fechaFin = LocalDate.parse(fechaFinTexto);
        } catch (DateTimeParseException e) {
            request.setAttribute("errorFecF","Las fechas ingresadas no son válidas.");
            request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
            return;
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            request.setAttribute("errorFecF","La fecha de fin debe ser posterior a la fecha de inicio.");
            request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
            return;
        }

        ProgramaFormacion programa = new ProgramaFormacion(nombre, descripcion, fechaInicio, fechaFin);

        Path imagenGuardada = null;
        boolean creado = false;

        try {
            Part archivo = request.getPart("imagen");
            String rutaImagen = guardarImagen(archivo);

            if (rutaImagen != null) {
                programa.setImagen(rutaImagen);

                String rutaFisica = getServletContext().getRealPath(rutaImagen);

                if (rutaFisica != null) {
                    imagenGuardada = Paths.get(rutaFisica);
                }
            }

            creado = controladorPrograma.altaPrograma(programa);

            if (creado) {
                response.sendRedirect(request.getContextPath() + "/componentes/principal.jsp");
                return;
            }
            request.setAttribute("errorNombre","No se pudo crear el programa. Verifique si el nombre ya existe.");

        } catch (Exception e) {
            log("Error al crear el programa o guardar su imagen.", e);
            request.setAttribute("errorImagen","No se pudo procesar la imagen. Verificá el archivo e intentá nuevamente.");

        } finally {
            // Si el programa no se creó, eliminar la imagen recién guardada.
            if (!creado && imagenGuardada != null) {
                try {
                    Files.deleteIfExists(imagenGuardada);
                } catch (IOException e) {
                    log("No se pudo eliminar la imagen temporal del programa.", e);
                }
            }
        }
        request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
    }
    private String guardarImagen(Part archivo) throws IOException {
        if (archivo == null
                || archivo.getSize() == 0
                || archivo.getSubmittedFileName() == null
                || archivo.getSubmittedFileName().isBlank()) {
            return null; // Imagen opcional.
        }
        String nombreOriginal = Paths.get(archivo.getSubmittedFileName()).getFileName().toString();
        int punto = nombreOriginal.lastIndexOf('.');
        if (punto < 0) {
            throw new IOException("La imagen no tiene extensión válida.");
        }

        String extension = nombreOriginal.substring(punto).toLowerCase(Locale.ROOT);

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")) {
            throw new IOException("Formato de imagen no permitido.");
        }
        byte[] contenido;
        try (var entrada = archivo.getInputStream()) {
            contenido = entrada.readAllBytes();
        }

        // Verificar que el contenido sea realmente una imagen.
        if (ImageIO.read(new ByteArrayInputStream(contenido)) == null) {
            throw new IOException("El archivo no contiene una imagen válida.");
        }

        String directorio = getServletContext().getRealPath("/imagenesProgramas");

        if (directorio == null) {
            throw new IOException("No se pudo resolver la carpeta de imágenes.");
        }

        Path carpeta = Paths.get(directorio);
        Files.createDirectories(carpeta);

        // Evita usar el nombre original del archivo.
        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = carpeta.resolve(nombreArchivo);

        Path temporal = Files.createTempFile(carpeta, "portada-", ".tmp");

        try {
            Files.write(temporal, contenido);
            Files.move(temporal,destino,StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }

        // Ruta relativa que se guardará en ProgramaFormacion.
        return "/imagenesProgramas/" + nombreArchivo;
    }
}
