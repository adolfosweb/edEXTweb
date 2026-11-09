package servlets;

import com.mycompany.edextweb.soap.ResultadoOperacionDTO;
import WebServices.ClienteSOAP;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.xml.ws.WebServiceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024,maxRequestSize = 6 * 1024 * 1024)
@WebServlet(name = "CrearProgramaServlet", urlPatterns = {"/crearPrograma"})
public class CrearProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String fechaInicioTexto = request.getParameter("fechaInicio");
        String fechaFinTexto = request.getParameter("fechaFin");

        nombre = nombre == null ? "" : nombre.trim();
        descripcion = descripcion == null ? "" : descripcion.trim();
        fechaInicioTexto = fechaInicioTexto == null ? "" : fechaInicioTexto.trim();
        fechaFinTexto = fechaFinTexto == null ? "" : fechaFinTexto.trim();

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
            volverFormulario(request, response);
            return;
        }
        LocalDate fechaInicio;
        LocalDate fechaFin;
        try {
            fechaInicio = LocalDate.parse(fechaInicioTexto);
            fechaFin = LocalDate.parse(fechaFinTexto);
        } catch (DateTimeParseException e) {
            request.setAttribute("errorFecF","Las fechas ingresadas no son válidas.");
            volverFormulario(request, response);
            return;
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            request.setAttribute("errorFecF","La fecha de fin debe ser posterior a la fecha de inicio.");
            volverFormulario(request, response);
            return;
        }
        Path imagenGuardada = null;
        boolean creado = false;
        try {
            Part archivo = request.getPart("imagen");
            String rutaImagen = guardarImagen(archivo);
            if (rutaImagen != null) {
                String rutaFisica = getServletContext().getRealPath(rutaImagen);
                if (rutaFisica == null) {
                    throw new IOException("No se pudo resolver la ubicación de la imagen.");
                }
                imagenGuardada = Paths.get(rutaFisica);
            }
            ResultadoOperacionDTO resultado =
                    ClienteSOAP.obtenerServicio().crearPrograma(
                            nombre,
                            descripcion,
                            fechaInicioTexto,
                            fechaFinTexto,
                            rutaImagen);

            if (resultado != null && resultado.isExitoso()) {
                creado = true;
                response.sendRedirect(request.getContextPath() + "/componentes/principal.jsp");
                return;
            }
            request.setAttribute("errorNombre",
                    resultado != null
                            ? resultado.getMensaje() : "No se recibió una respuesta válida del servidor central.");
        } catch (WebServiceException e) {
            log("Error al comunicarse con el servidor central al crear un programa.", e);
            request.setAttribute("errorNombre","No se pudo comunicar con el servidor central. Intente nuevamente.");
        } catch (IOException e) {
            log("Error al procesar la imagen del programa.", e);
            request.setAttribute("errorImagen",
                    "No se pudo procesar la imagen. Verificá el archivo e intentá nuevamente.");

        } catch (Exception e) {
            log("Error inesperado al crear el programa.", e);
            request.setAttribute("errorNombre",
                    "Ocurrió un error al crear el programa.");
        } finally {
            if (!creado && imagenGuardada != null) {
                try {
                    Files.deleteIfExists(imagenGuardada);
                } catch (IOException e) {
                    log("No se pudo eliminar la imagen del programa.", e);
                }
            }
        }
        volverFormulario(request, response);
    }

    private void volverFormulario(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        request.getRequestDispatcher("/componentes/crearPrograma.jsp").forward(request, response);
    }

    private String guardarImagen(Part archivo) throws IOException {
        if (archivo == null || archivo.getSize() == 0
                || archivo.getSubmittedFileName() == null
                || archivo.getSubmittedFileName().isBlank()) {
            return null;
        }
        String nombreOriginal = Paths.get(archivo.getSubmittedFileName()).getFileName().toString();
        int punto = nombreOriginal.lastIndexOf('.');
        if (punto < 0) {
            throw new IOException("La imagen no tiene extensión válida.");
        }
        String extension = nombreOriginal.substring(punto).toLowerCase(Locale.ROOT);
        if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
            throw new IOException("Formato de imagen no permitido.");
        }
        byte[] contenido;
        try (var entrada = archivo.getInputStream()) {
            contenido = entrada.readAllBytes();
        }
        if (ImageIO.read(new ByteArrayInputStream(contenido)) == null) {
            throw new IOException("El archivo no contiene una imagen válida.");
        }
        String directorio = getServletContext().getRealPath("/imagenesProgramas");
        if (directorio == null) {
            throw new IOException("No se pudo resolver la carpeta de imágenes.");
        }
        Path carpeta = Paths.get(directorio);
        Files.createDirectories(carpeta);

        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = carpeta.resolve(nombreArchivo);

        Path temporal = Files.createTempFile(carpeta, "portada-", ".tmp");
        try {
            Files.write(temporal, contenido);
            Files.move(
                    temporal,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }
        return "/imagenesProgramas/" + nombreArchivo;
    }
}
