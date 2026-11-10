package Controladores;

import WebServices.Configuracion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class Conexion {

    private static Conexion instancia;
    private EntityManagerFactory emf;

    private Conexion() {
        try {
            Map<String, String> cfg = new HashMap<>();
            cfg.put("jakarta.persistence.jdbc.url", Configuracion.get("db.url"));
            cfg.put("jakarta.persistence.jdbc.user", Configuracion.get("db.user"));
            cfg.put("jakarta.persistence.jdbc.password", Configuracion.get("db.password"));

            emf = Persistence.createEntityManagerFactory("my_persistence_unit", cfg);
            System.out.println("EntityManagerFactory creado correctamente.");
        } catch (Exception e) {
            System.err.println("ERROR AL INICIALIZAR JPA:");
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar JPA", e);
        }
    }

    public EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory no fue inicializado.");
        }
        return emf.createEntityManager();
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
}