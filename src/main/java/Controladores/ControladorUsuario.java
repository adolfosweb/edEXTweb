/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Logica.Usuario;
import Logica.Estudiante;
import Logica.InscripcionCurso;
import Logica.InscripcionPrograma;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.Instituto;


public class ControladorUsuario implements IControladorUsuario {

    public ControladorUsuario() {
    }

    @Override
public boolean altaUsuario(Usuario usuario, String nombreInstituto) {

    if (usuario == null || usuario.getNickname() == null
            || usuario.getNickname().trim().isEmpty()) {
        return false;
    }

    EntityManager em = Conexion.getInstancia().getEntityManager();
    EntityTransaction tx = em.getTransaction();

    try {
        tx.begin();

        // Verificar nickname
        Usuario existente = em.find(Usuario.class, usuario.getNickname());

        if (existente != null) {
            tx.rollback();
            return false;
        }

        // Verificar correo
        Long cantidad = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u "
                + "WHERE u.correoElectronico = :correo",
                Long.class
        ).setParameter("correo", usuario.getCorreoElectronico())
         .getSingleResult();

        if (cantidad > 0) {
            tx.rollback();
            return false;
        }

        // Si es docente, buscar el instituto DENTRO de esta sesión
        if (usuario instanceof Docente docente) {

            Instituto instituto = em.find(Instituto.class, nombreInstituto);

            if (instituto == null) {
                tx.rollback();
                return false;
            }

            // ESTE es el lado dueño de la relación
            docente.agregarInstituto(instituto);
        }

        em.persist(usuario);

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
    public Usuario buscarUsuario(String nickname) {

    if (nickname == null || nickname.trim().isEmpty()) {
        return null;
    }

    EntityManager em = Conexion.getInstancia().getEntityManager();

    try {

        Usuario usuario = em.find(Usuario.class, nickname);

        if (usuario instanceof Estudiante estudiante) {

            // =========================
            // ESTUDIANTE
            // =========================

            estudiante.getCursos().size();
            estudiante.getProgramas().size();

            for (InscripcionCurso ic : estudiante.getCursos()) {

                if (ic.getCurso() != null) {
                    ic.getCurso().getNombre();
                }
            }

            for (InscripcionPrograma ip : estudiante.getProgramas()) {

                if (ip.getPrograma() != null) {
                    ip.getPrograma().getNombre();
                }
            }

        } else if (usuario instanceof Docente docente) {

            // =========================
            // DOCENTE
            // =========================

            docente.getInstitutos().size();
            docente.getEdiciones().size();

            for (EdicionCurso edicion : docente.getEdiciones()) {

                if (edicion.getCurso() != null) {
                    edicion.getCurso().getNombre();
                }
            }
        }

        return usuario;

    } finally {
        em.close();
    }
}

    @Override
    public void modificarUsuario(Usuario usuario) {

        if (usuario == null || usuario.getNickname() == null) {
            return;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.merge(usuario);

            tx.commit();

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();

        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarUsuario(String nickname) {

        if (nickname == null || nickname.trim().isEmpty()) {
            return;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Usuario usuario = em.find(Usuario.class, nickname);

            if (usuario != null) {
                em.remove(usuario);
            }

            tx.commit();

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();

        } finally {
            em.close();
        }
    }

    @Override
    public Map<String, Usuario> listarUsuarios() {

        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {

            List<Usuario> lista = em.createQuery(
                    "SELECT u FROM Usuario u",
                    Usuario.class
            ).getResultList();

            Map<String, Usuario> mapaUsuarios = new HashMap<>();

            for (Usuario u : lista) {
                mapaUsuarios.put(u.getNickname(), u);
            }

            return mapaUsuarios;

        } finally {
            em.close();
        }
    }
}
