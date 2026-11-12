
package WebServices;

import Presentacion.Principal;
import javax.swing.SwingUtilities;

public class MainServidor {

    public static void main(String[] args) {

        // Iniciar el servicio SOAP en un hilo independiente
        Thread servidor = new Thread(() -> {
            try {
                ServidorSOAP.main(args);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("El servidor SOAP fue interrumpido.");
            } catch (Exception e) {
                System.err.println("Error iniciando SOAP:");
                e.printStackTrace();
            }
        });

        servidor.setName("Servidor-SOAP");
        servidor.start();

        // Iniciar la aplicación gráfica Swing
        SwingUtilities.invokeLater(() -> {
            Principal principal = new Principal();
            principal.setVisible(true);
        });
    }
}
