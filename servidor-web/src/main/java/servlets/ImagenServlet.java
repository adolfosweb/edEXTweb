package servlets;

import WebServices.ClienteSOAP;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "ImagenServlet", urlPatterns = {"/imagen"})
public class ImagenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ruta = request.getParameter("ruta");
        String tipo = ruta == null ? null : tipoPorExtension(ruta.toLowerCase(Locale.ROOT));
        if (tipo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            byte[] datos = ClienteSOAP.obtenerServicio().obtenerImagen(ruta);
            if (datos == null || datos.length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType(tipo);
            response.setHeader("Cache-Control", "private, max-age=300");
            response.setContentLength(datos.length);
            response.getOutputStream().write(datos);
        } catch (WebServiceException e) {
            getServletContext().log("No se pudo obtener la imagen " + ruta, e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private String tipoPorExtension(String ruta) {
        if (ruta.endsWith(".png")) return "image/png";
        if (ruta.endsWith(".jpg") || ruta.endsWith(".jpeg")) return "image/jpeg";
        return null;
    }
}