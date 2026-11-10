package WebServices;

import jakarta.xml.ws.Endpoint;
import java.util.concurrent.CountDownLatch;

public class ServidorSOAP {

    public static void main(String[] args) throws InterruptedException {

        String direccion = Configuracion.get("servidor.url");

        Endpoint.publish(direccion, new EdextWebService());

        System.out.println("Servidor SOAP iniciado.");
        System.out.println("WSDL: " + direccion + "?wsdl");

        new CountDownLatch(1).await();
    }
}