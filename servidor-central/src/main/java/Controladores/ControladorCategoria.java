package Controladores;

import Logica.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ControladorCategoria implements IControladorCategoria {

    public ControladorCategoria() {
    }

    @Override
    public boolean altaCategoria(Categoria categoria) {

        if (categoria == null
                || categoria.getNombre() == null
                || categoria.getNombre().trim().isEmpty()) {
            return false;
        }

        String nombre = categoria.getNombre().trim();

        if (buscarCategoria(nombre) != null) {
            return false;
        }

        categoria.setNombre(nombre);

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(categoria);
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

    @Override
    public Categoria buscarCategoria(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            return em.find(Categoria.class, nombre.trim());
        } finally {
            em.close();
        }
    }
}