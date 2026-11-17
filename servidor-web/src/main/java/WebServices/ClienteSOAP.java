package WebServices;

import com.mycompany.edextweb.soap.EdextService;
import com.mycompany.edextweb.soap.EdextWebService;
import jakarta.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

public class ClienteSOAP {

    public static EdextWebService obtenerServicio() {

        String direccion = Configuracion.get("servidor.url");
        try {
            EdextService servicio = new EdextService(new URL(direccion + "?wsdl"));
            EdextWebService puerto = servicio.getEdextWebServicePort();

            ((BindingProvider) puerto).getRequestContext().put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY, direccion);
            return puerto;

        } catch (MalformedURLException e) {
            throw new IllegalStateException("servidor.url no es una URL válida: " + direccion, e);
        } catch (RuntimeException e) {
            System.err.println("ClienteSOAP: no se pudo crear el cliente para " + direccion);
            e.printStackTrace();
            throw e;
        }
    }
}