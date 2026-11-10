package WebServices;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuracion {

    private static final String NOMBRE_ARCHIVO = "servidor.properties";

    private static final Path ARCHIVO =
            Paths.get(System.getProperty("user.home"), ".edEXT", NOMBRE_ARCHIVO);
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Files.newInputStream(ARCHIVO)) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(
                "No se pudo leer la configuración en " + ARCHIVO, e);
        }
    }

    public static String get(String clave) {
        String valor = props.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException(
                "Falta la clave '" + clave + "' en " + ARCHIVO);
        }
        return valor.trim();
    }
}