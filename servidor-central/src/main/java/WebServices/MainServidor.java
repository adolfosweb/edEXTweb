
package WebServices;

import Presentacion.Principal;
import javax.swing.SwingUtilities;

public class MainServidor {

    public static void main(String[] args) {

    try {
        for (javax.swing.UIManager.LookAndFeelInfo info :
                javax.swing.UIManager.getInstalledLookAndFeels()) {

            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

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

    SwingUtilities.invokeLater(() -> {
        Principal principal = new Principal();
        principal.setVisible(true);
    });
}
}
