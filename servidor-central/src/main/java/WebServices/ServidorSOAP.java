package WebServices;

import jakarta.xml.ws.Endpoint;
import java.util.concurrent.CountDownLatch;

public class ServidorSOAP {

    public static void main(String[] args) throws InterruptedException {

        String direccion = "http://0.0.0.0:8081/edext/edext";

        Endpoint endpoint = Endpoint.publish(direccion,new EdextWebService());

        System.out.println("Servidor SOAP iniciado.");
        System.out.println("WSDL: http://localhost:8081/edext/edext?wsdl");

        new CountDownLatch(1).await();
    }
}