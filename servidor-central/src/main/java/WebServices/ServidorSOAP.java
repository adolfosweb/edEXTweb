package WebServices;

import Controladores.Conexion;
import jakarta.xml.ws.Endpoint;
import java.util.concurrent.CountDownLatch;

public class ServidorSOAP {

    public static void main(String[] args) throws InterruptedException {
        
        // Inicializar JPA y la conexión a la base de datos.
        Conexion.getInstancia();
        
        String direccion = Configuracion.get("servidor.url");

        Endpoint.publish(direccion, new EdextWebService());

        System.out.println("Servidor SOAP iniciado.");
        System.out.println("WSDL: " + direccion + "?wsdl");

        new CountDownLatch(1).await();
    }
}