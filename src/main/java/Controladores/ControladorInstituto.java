/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;
import Logica.Instituto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author manug
 */
public class ControladorInstituto implements IControladorInstituto {

    public ControladorInstituto() {
    }

    @Override
    public boolean altaInstituto(Instituto instituto) {

        if (instituto == null || instituto.getNombre() == null
                || buscarInstituto(instituto.getNombre()) != null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(instituto);
            tx.commit();
            return true;

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();
            return false;

        } finally {
            em.close();
        }
    }

//    @Override
//    public Instituto buscarInstituto(String nombre) {
//
//        if (nombre == null || nombre.trim().isEmpty()) {
//            return null;
//        }
//
//        EntityManager em = Conexion.getInstancia().getEntityManager();
//
//        try {
//            return em.find(Instituto.class, nombre);
//        } finally {
//            em.close();
//        }
//    }
    
    @Override
    public Instituto buscarInstituto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            // Carga el Instituto e inicializa su lista 'cursos' dentro de la misma sesión
            List<Instituto> resultados = em.createQuery(
                "SELECT DISTINCT i FROM Instituto i LEFT JOIN FETCH i.cursos WHERE i.nombre = :nombre", 
                Instituto.class)
                .setParameter("nombre", nombre)
                .getResultList();

            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Instituto> listarInstitutos() {

        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            List<Instituto> lista = em.createQuery(
                    "SELECT i FROM Instituto i",
                    Instituto.class
            ).getResultList();

            Map<String, Instituto> mapaInstitutos = new HashMap<>();

            for (Instituto i : lista) {
                mapaInstitutos.put(i.getNombre(), i);
            }

            return mapaInstitutos;

        } finally {
            em.close();
        }
    }
}
