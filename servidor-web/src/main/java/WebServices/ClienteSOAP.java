package WebServices;

import com.mycompany.edextweb.soap.EdextService;
import com.mycompany.edextweb.soap.EdextWebService;
import jakarta.xml.ws.BindingProvider;

public class ClienteSOAP {

    private static final String DIRECCION_SERVIDOR = "http://localhost:8081/edext/edext";

    public static EdextWebService obtenerServicio() {

        EdextService servicio = new EdextService();
        EdextWebService puerto = servicio.getEdextWebServicePort();

        BindingProvider binding = (BindingProvider) puerto;

        binding.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,DIRECCION_SERVIDOR);
        return puerto;
    }
}