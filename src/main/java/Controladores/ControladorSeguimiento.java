
package Controladores;

import Logica.Seguimiento;
import Logica.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class ControladorSeguimiento {

    public boolean estaSiguiendo(String nicknameSeguidor,
                                 String nicknameSeguido) {

        if (!datosValidos(nicknameSeguidor, nicknameSeguido)) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            Long cantidad = em.createQuery(
                "SELECT COUNT(s) FROM Seguimiento s " +
                "WHERE s.seguidor.nickname = :seguidor " +
                "AND s.seguido.nickname = :seguido",
                Long.class
            )
            .setParameter("seguidor", nicknameSeguidor)
            .setParameter("seguido", nicknameSeguido)
            .getSingleResult();

            return cantidad > 0;

        } finally {
            em.close();
        }
    }

    public boolean seguir(String nicknameSeguidor,
                          String nicknameSeguido) {

        if (!datosValidos(nicknameSeguidor, nicknameSeguido)) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Usuario seguidor = em.find(Usuario.class, nicknameSeguidor);
            Usuario seguido = em.find(Usuario.class, nicknameSeguido);

            if (seguidor == null || seguido == null) {
                tx.rollback();
                return false;
            }

            if (nicknameSeguidor.equals(nicknameSeguido)) {
                tx.rollback();
                return false;
            }

            List<Seguimiento> existentes = em.createQuery(
                "SELECT s FROM Seguimiento s " +
                "WHERE s.seguidor.nickname = :seguidor " +
                "AND s.seguido.nickname = :seguido",
                Seguimiento.class
            )
            .setParameter("seguidor", nicknameSeguidor)
            .setParameter("seguido", nicknameSeguido)
            .setMaxResults(1)
            .getResultList();

            if (!existentes.isEmpty()) {
                tx.rollback();
                return false;
            }

            em.persist(new Seguimiento(seguidor, seguido));
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

    public boolean dejarDeSeguir(String nicknameSeguidor,
                                 String nicknameSeguido) {

        if (!datosValidos(nicknameSeguidor, nicknameSeguido)) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<Seguimiento> relaciones = em.createQuery(
                "SELECT s FROM Seguimiento s " +
                "WHERE s.seguidor.nickname = :seguidor " +
                "AND s.seguido.nickname = :seguido",
                Seguimiento.class
            )
            .setParameter("seguidor", nicknameSeguidor)
            .setParameter("seguido", nicknameSeguido)
            .setMaxResults(1)
            .getResultList();

            if (relaciones.isEmpty()) {
                tx.rollback();
                return false;
            }

            em.remove(relaciones.get(0));
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

    private boolean datosValidos(String seguidor, String seguido) {
        return seguidor != null && !seguidor.trim().isEmpty()
            && seguido != null && !seguido.trim().isEmpty();
    }
}
