package WebServices;

import com.mycompany.edextweb.soap.EdextService;
import com.mycompany.edextweb.soap.EdextWebService;
import jakarta.xml.ws.BindingProvider;

public class ClienteSOAP {

    public static EdextWebService obtenerServicio() {

        EdextService servicio = new EdextService();
        EdextWebService puerto = servicio.getEdextWebServicePort();

        BindingProvider binding = (BindingProvider) puerto;
        binding.getRequestContext().put(
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
            Configuracion.get("servidor.url"));
        return puerto;
    }
}