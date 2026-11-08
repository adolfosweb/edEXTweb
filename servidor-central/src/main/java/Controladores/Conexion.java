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
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
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

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

} 