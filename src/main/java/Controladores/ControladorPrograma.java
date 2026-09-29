/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import Logica.ProgramaFormacion;
import Logica.Curso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author manug
 */
public class ControladorPrograma implements IControladorPrograma {

    public ControladorPrograma() {
    }
    
    @Override
    public boolean altaPrograma(ProgramaFormacion programa) {
        if(programa == null || programa.getNombre() == null || programa.getNombre().trim().isEmpty()){
            return false;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            ProgramaFormacion existente = em.find(ProgramaFormacion.class, programa.getNombre());

            if(existente != null){
                tx.rollback();
                return false;
            }

            em.persist(programa);

            tx.commit();
            return true;

        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            e.printStackTrace();
            return false;

        }finally{
            em.close();
        }
    }
    
    @Override
    public ProgramaFormacion buscarPrograma(String nombre) {
        if(nombre == null || nombre.trim().isEmpty()){
            return null;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try{
            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombre);
            if(programa != null){
                // Inicializar relaciones lazy antes de cerrar el EntityManager
                programa.getCursos().size();
                programa.getInscriptos().size();
            }
            return programa;
        }finally{
            em.close();
        }
    }

    @Override
    public boolean modificarPrograma(ProgramaFormacion programa) {
        if(programa == null || programa.getNombre() == null){
            return false;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            ProgramaFormacion existente = em.find(ProgramaFormacion.class, programa.getNombre());
            if(existente == null){
                tx.rollback();
                return false;
            }
            em.merge(programa);

            tx.commit();
            return true;
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }

            e.printStackTrace();
            return false;
        }finally{
            em.close();
        }
    }
    
    @Override
    public boolean eliminarPrograma(String nombre) {
        if(nombre == null || nombre.trim().isEmpty()){
            return false;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombre);
            if(programa == null){
                tx.rollback();
                return false;
            }

            em.remove(programa);

            tx.commit();
            return true;
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }

            e.printStackTrace();
            return false;
        }finally{
            em.close();
        }
    }

    @Override
public Map<String, ProgramaFormacion> listarProgramas() {
    EntityManager em = Conexion.getInstancia().getEntityManager();

    try {
        List<ProgramaFormacion> lista =
                em.createQuery(
                    "SELECT DISTINCT p FROM ProgramaFormacion p " +
                    "LEFT JOIN FETCH p.cursos",
                    ProgramaFormacion.class
                ).getResultList();

        Map<String, ProgramaFormacion> mapaProgramas = new HashMap<>();

        for (ProgramaFormacion p : lista) {
            mapaProgramas.put(p.getNombre(), p);
        }

        return mapaProgramas;

    } finally {
        em.close();
    }
}
    
    @Override
    public boolean agregarCurso(String nombrePrograma, String nombreCurso) {
        if(nombrePrograma == null || nombreCurso == null){
            return false;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            ProgramaFormacion programa = em.find(ProgramaFormacion.class, nombrePrograma);
            Curso curso = em.find(Curso.class, nombreCurso);
            if(programa == null || curso == null){
                tx.rollback();
                return false;
            }

            if(programa.agregarCurso(curso)){
                tx.commit();
                return true;
            }
            return false;
            
        }catch(Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            
            e.printStackTrace();
            return false;
        }finally{
            em.close();
        }
    }
}
