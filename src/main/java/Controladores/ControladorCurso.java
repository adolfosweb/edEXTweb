package Controladores;

import Logica.Curso;
import Logica.Instituto;
import Logica.EdicionCurso;
import Logica.InscripcionCurso;
import Logica.Estudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;



public class ControladorCurso implements IControladorCurso {

    public ControladorCurso() {
    }

    @Override
    public boolean altaCurso(Curso curso) {
        if (curso == null || curso.getNombre() == null || buscarCurso(curso.getNombre()) != null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            // Re-asociar el Instituto al contexto de persistencia si viene desconectado (detached)
            if (curso.getInstituto() != null) {
                Instituto inst = em.find(Instituto.class, curso.getInstituto().getNombre());
                curso.setInstituto(inst);
            }
                em.persist(curso);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    
    @Override
    public Curso buscarCurso(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            // Se realiza JOIN FETCH para cargar las previas antes de cerrar el em
            String jpql = "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas WHERE c.nombre = :nombre";
            List<Curso> resultados = em.createQuery(jpql, Curso.class)
                                       .setParameter("nombre", nombre)
                                       .getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }
//    @Override
//    public Curso buscarCurso(String nombre) {
//        if (nombre == null || nombre.trim().isEmpty()) {
//            return null;
//        }
//        
//        EntityManager em = Conexion.getInstancia().getEntityManager();
//        try {
//            return em.find(Curso.class, nombre);
//        } finally {
//            em.close();
//        }
//    }

    @Override
    public boolean modificarCurso(Curso curso) {
        if (curso == null || curso.getNombre() == null || buscarCurso(curso.getNombre()) == null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(curso); // Modifica la entidad en la base de datos
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminarCurso(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Curso curso = em.find(Curso.class, nombre);
            if (curso != null) {
                em.remove(curso);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean altaEdicion(EdicionCurso edicion) {
        if (edicion == null || edicion.getNombre() == null || buscarEdicion(edicion.getNombre()) != null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(edicion);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public EdicionCurso buscarEdicion(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            return em.find(EdicionCurso.class, nombre);
        } finally {
            em.close();
        }
    }
    
    @Override
    public Map<String, Curso> listarCursos() {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            // DISTINCT evita duplicados al hacer JOIN FETCH con colecciones
            String jpql = "SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.previas";
            List<Curso> lista = em.createQuery(jpql, Curso.class).getResultList();
            
            Map<String, Curso> mapaCursos = new HashMap<>();
            for (Curso c : lista) {
                mapaCursos.put(c.getNombre(), c);
            }
            return mapaCursos;
        } finally {
            em.close();
        }
    }

//    @Override
//    public Map<String, Curso> listarCursos() {
//        EntityManager em = Conexion.getInstancia().getEntityManager();
//        try {
//            // Consulta JPQL para traer la lista desde MySQL
//            List<Curso> lista = em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
//            
//            Map<String, Curso> mapaCursos = new HashMap<>();
//            for (Curso c : lista) {
//                mapaCursos.put(c.getNombre(), c);
//            }
//            return mapaCursos;
//        } finally {
//            em.close();
//        }
//    }

    @Override
    public Map<String, EdicionCurso> listarEdiciones() {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<EdicionCurso> lista = em.createQuery("SELECT e FROM EdicionCurso e", EdicionCurso.class).getResultList();
            
            Map<String, EdicionCurso> mapaEdiciones = new HashMap<>();
            for (EdicionCurso e : lista) {
                mapaEdiciones.put(e.getNombre(), e);
            }
            return mapaEdiciones;
        } finally {
            em.close();
        }
    }
    
    
    @Override
    public InscripcionCurso buscarInscripcion(String nickEstudiante, String nombreEdicion) {
        if (nickEstudiante == null || nombreEdicion == null) {
            return null;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            String jpql = "SELECT i FROM InscripcionCurso i WHERE i.estudiante.nickname = :nick AND i.edicionCurso.nombre = :edicion";
            List<InscripcionCurso> lista = em.createQuery(jpql, InscripcionCurso.class)
                    .setParameter("nick", nickEstudiante)
                    .setParameter("edicion", nombreEdicion)
                    .getResultList();

            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean inscribirEstudiante(InscripcionCurso inscripcion) {
        if (inscripcion == null || inscripcion.getEstudiante() == null || inscripcion.getCurso() == null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1. Re-asociar las entidades
            Estudiante est = em.find(Estudiante.class, inscripcion.getEstudiante().getNickname());
            EdicionCurso ed = em.find(EdicionCurso.class, inscripcion.getCurso().getNombre());

            if (est == null || ed == null) {
                return false;
            }

            // 2. Validar que la edición tenga cupo disponible (si tiene cupo asignado > 0)
            if (ed.getCupo() > 0 && ed.getInscriptos().size() >= ed.getCupo()) {
                tx.rollback();
                return false; // Cupo agotado
            }

            inscripcion.setEstudiante(est);
            inscripcion.setCurso(ed);

            em.persist(inscripcion);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    
   //Metodos con Datatypes para version WEB
    
    
    @Override
public List<Datatypes.DTCurso> obtenerListaCursosDT() {
    Map<String, Logica.Curso> mapaCursos = listarCursos(); // Tu método existente
    List<Datatypes.DTCurso> resultado = new ArrayList<>();

    for (Logica.Curso c : mapaCursos.values()) {
        List<String> previasStr = new ArrayList<>();
        if (c.getPrevias() != null) {
            for (Logica.Curso previa : c.getPrevias()) {
                previasStr.add(previa.getNombre());
            }
        }

        resultado.add(new Datatypes.DTCurso(
            c.getNombre(),
            c.getDescripcion(),
            c.getDuracion(),
            c.getCantHoras(),
            c.getCantCreditos(),
            c.getFecRegistro(),
            c.getUrl(),
            c.getInstituto() != null ? c.getInstituto().getNombre() : "Sin Instituto",
            null,
            previasStr,
            new ArrayList<String>()
        ));
    }
    return resultado;
}
    
    
    @Override
    public Datatypes.DTCurso obtenerInformacionCurso(String nombre) {
        Logica.Curso c = buscarCurso(nombre);
        if (c == null) return null;

        List<String> previasStr = new ArrayList<>();
        if (c.getPrevias() != null) {
            for (Logica.Curso previa : c.getPrevias()) {
                previasStr.add(previa.getNombre());
            }
        }

        return new Datatypes.DTCurso(
            c.getNombre(),
            c.getDescripcion(),
            c.getDuracion(),
            c.getCantHoras(),
            c.getCantCreditos(),
            c.getFecRegistro(),
            c.getUrl(),
            c.getInstituto() != null ? c.getInstituto().getNombre() : null,
            null,                       // Imagen
            previasStr,                 // Previas
            new ArrayList<String>()     // Categorías
        );
    }

    @Override
    public List<Datatypes.DTEdicionCurso> obtenerEdicionesCurso(String nombreCurso) {
        Logica.Curso c = buscarCurso(nombreCurso);
        List<Datatypes.DTEdicionCurso> resultado = new ArrayList<>();
        if (c != null && c.getEdiciones() != null) {
            for (Logica.EdicionCurso ed : c.getEdiciones()) {
                resultado.add(new Datatypes.DTEdicionCurso(
                    ed.getNombre(),
                    ed.getFechaInicio(),
                    ed.getFechaFin(),
                    ed.getCupo(),
                    ed.getFechaPublicacion(),
                    c.getNombre(),
                    null,
                    new ArrayList<>()
                ));
            }
        }
        return resultado;
    }

    @Override
    public List<Datatypes.DTProgramaFormacion> obtenerProgramasCurso(String nombreCurso) {
        Logica.Curso c = buscarCurso(nombreCurso);
        List<Datatypes.DTProgramaFormacion> resultado = new ArrayList<>();
        if (c != null && c.getProgramas() != null) {
            for (Logica.ProgramaFormacion p : c.getProgramas()) {
                resultado.add(new Datatypes.DTProgramaFormacion(
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getFechaInicio(),
                    p.getFechaFin(),
                    p.getFechaAlta(),
                    null,
                    new ArrayList<>(),
                    new ArrayList<>()
                ));
            }
        }
        return resultado;
}
    
}

