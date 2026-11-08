package Controladores;

import Logica.TokenRecordarme;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDateTime;

public class ControladorTokenRecordarme {

    public void guardarToken(TokenRecordarme token) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(token);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public TokenRecordarme buscarTokenValido(String tokenHash) {
        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            return em.createQuery(
                    "SELECT t FROM TokenRecordarme t "
                    + "WHERE t.tokenHash = :hash "
                    + "AND t.fechaExpiracion > :ahora",
                    TokenRecordarme.class
            )
            .setParameter("hash", tokenHash)
            .setParameter("ahora", LocalDateTime.now())
            .getResultStream()
            .findFirst()
            .orElse(null);
        } finally {
            em.close();
        }
    }

    public void eliminarToken(String tokenHash) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.createQuery(
                    "DELETE FROM TokenRecordarme t "
                    + "WHERE t.tokenHash = :hash"
            )
            .setParameter("hash", tokenHash)
            .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}