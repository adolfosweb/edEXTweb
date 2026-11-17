package servlets;

import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.ResultadoBusquedaDTO;

import jakarta.xml.ws.WebServiceException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Obtiene la URL de la imagen de un curso (la misma que usa el buscador: /imagen?ruta=...).
 * Los listados y detalles no traen la imagen, así que se toma del resultado de búsqueda del curso.
 */
public final class ImagenesCurso {

    // nombre del curso -> ruta relativa de la imagen ("" si no tiene). Evita repetir llamadas SOAP.
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    private ImagenesCurso() {
    }

    /** Devuelve la URL de la imagen del curso, o "" si no tiene o no se pudo consultar. */
    public static String urlImagen(EdextWebService servicio, String nombreCurso, String contextPath) {
        if (nombreCurso == null) {
            return "";
        }

        String ruta = CACHE.get(nombreCurso);
        if (ruta == null) {
            ruta = "";
            try {
                for (ResultadoBusquedaDTO r : servicio.buscar(nombreCurso, "", "")) {
                    if ("curso".equalsIgnoreCase(r.getTipo()) && nombreCurso.equals(r.getNombre())
                            && r.getImagen() != null && !r.getImagen().isBlank()) {
                        ruta = r.getImagen();
                        break;
                    }
                }
                CACHE.put(nombreCurso, ruta);
            } catch (WebServiceException e) {
                return "";
            }
        }

        if (ruta.isEmpty()) {
            return "";
        }
        return contextPath + "/imagen?ruta=" + URLEncoder.encode(ruta, StandardCharsets.UTF_8);
    }
}