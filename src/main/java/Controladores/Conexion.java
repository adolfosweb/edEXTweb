/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

/**
 *
 * @author adolfo
 */


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Conexion {

    private static Conexion instancia;
    private EntityManagerFactory emf;

    private Conexion() {
        try {
            // Usa el nombre exacta de tu persistence.xml
            emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        } catch (Exception e) {
            System.err.println("Error al inicializar la unidad de persistencia:");
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
