package Controladores;

import Logica.Curso;
import Logica.Instituto;
import Logica.EdicionCurso;
import Logica.InscripcionCurso;
import Logica.Estudiante;
import Logica.Categoria;
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
            if (curso.getInstituto() != null) {
                Instituto instituto = em.find(Instituto.class,curso.getInstituto().getNombre());
                curso.setInstituto(instituto);
            }

            List<Categoria> categorias = new ArrayList<>();
            for (Categoria categoria : curso.getCategorias()) {
                Categoria gestionada = em.find(Categoria.class,categoria.getNombre());
                if (gestionada != null) {
                    categorias.add(gestionada);
                }
            }

            curso.setCategorias(categorias);
            em.persist(curso);
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
    public Curso buscarCurso(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            // Cargar el curso y sus previas.
            String jpql = """
                SELECT DISTINCT c
                FROM Curso c
                LEFT JOIN FETCH c.previas
                WHERE c.nombre = :nombre
            """;

            List<Curso> resultados = em.createQuery(jpql, Curso.class).setParameter("nombre", nombre).getResultList();
            if (resultados.isEmpty()) {
                return null;
            }
            Curso curso = resultados.get(0);

            // Cargar las categorías del curso.
            em.createQuery("""
                SELECT DISTINCT c
                FROM Curso c
                LEFT JOIN FETCH c.categorias
                WHERE c.nombre = :nombre
            """, Curso.class).setParameter("nombre", nombre).getSingleResult();
            return curso;
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
        if (curso == null || curso.getNombre() == null) {
            return false;
        }
        
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Recuperamos la entidad que ya está administrada por JPA.
            Curso existente = em.find(Curso.class, curso.getNombre());
            if (existente == null) {
                tx.rollback();
                return false;
            }

            // Actualizamos los atributos simples.
            existente.setDescripcion(curso.getDescripcion());
            existente.setDuracion(curso.getDuracion());
            existente.setCantHoras(curso.getCantHoras());
            existente.setCantCreditos(curso.getCantCreditos());
            existente.setFecRegistro(curso.getFecRegistro());
            existente.setUrl(curso.getUrl());

            // Actualizamos el instituto.
            if (curso.getInstituto() != null) {
                Instituto instituto = em.find(Instituto.class,curso.getInstituto().getNombre());
                if (instituto == null) {
                    tx.rollback();
                    return false;
                }
                existente.setInstituto(instituto);
            } else {
                tx.rollback();
                return false;
            }

            // Recuperamos las previas dentro del contexto de persistencia.
            List<Curso> previas = new ArrayList<>();
            for (Curso previa : curso.getPrevias()) {
                Curso previaGestionada = em.find(Curso.class,previa.getNombre());
                if (previaGestionada != null) {
                    previas.add(previaGestionada);
                }
            }
            existente.setPrevias(previas);

            // Recuperamos las categorías dentro del contexto de persistencia.
            List<Categoria> categorias = new ArrayList<>();
            for (Categoria categoria : curso.getCategorias()) {
                Categoria categoriaGestionada = em.find(Categoria.class,categoria.getNombre());
                if (categoriaGestionada != null) {
                    categorias.add(categoriaGestionada);
                }
            }
            existente.setCategorias(categorias);
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
            List<EdicionCurso> lista = em.createQuery(
                "SELECT DISTINCT e FROM EdicionCurso e "
                + "LEFT JOIN FETCH e.curso c "
                + "LEFT JOIN FETCH c.instituto",
                EdicionCurso.class
            ).getResultList();
            Map<String, EdicionCurso> mapaEdiciones = new HashMap<>();
            for (EdicionCurso edicion : lista) {
                mapaEdiciones.put(edicion.getNombre(), edicion);
            }
            return mapaEdiciones;
        } finally {
            em.close();
        }
    }
    
    @Override
    public Map<String, Categoria> listarCategorias() {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<Categoria> lista = em.createQuery(
                "SELECT c FROM Categoria c ORDER BY c.nombre",
                Categoria.class
            ).getResultList();

            Map<String, Categoria> mapa = new HashMap<>();
            for (Categoria categoria : lista) {
                mapa.put(categoria.getNombre(), categoria);
            }
            return mapa;
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
        if (inscripcion == null
                || inscripcion.getEstudiante() == null
                || inscripcion.getCurso() == null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Estudiante est = em.find(
                    Estudiante.class,
                    inscripcion.getEstudiante().getNickname()
            );

            EdicionCurso ed = em.find(
                    EdicionCurso.class,
                    inscripcion.getCurso().getNombre()
            );

            if (est == null || ed == null) {
                tx.rollback();
                return false;
            }
            InscripcionCurso existente = buscarInscripcion(
                    est.getNickname(),
                    ed.getNombre()
            );

            if (existente != null) {
                tx.rollback();
                return false;
            }


            if (ed.getCupo() > 0
                    && ed.getInscriptos().size() >= ed.getCupo()) {
                tx.rollback();
                return false;
            }

            inscripcion.setEstudiante(est);
            inscripcion.setCurso(ed);
            if (inscripcion.getEstado() == null) {
                inscripcion.setEstado(Logica.EstadoInscripcion.INSCRIPTO);
            }

            em.persist(inscripcion);

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
    public boolean cambiarEstadoInscripcion(Long idInscripcion, Logica.EstadoInscripcion estado) {

        if (idInscripcion == null || estado == null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            InscripcionCurso inscripcion =
                    em.find(InscripcionCurso.class, idInscripcion);

            if (inscripcion == null) {
                tx.rollback();
                return false;
            }

            inscripcion.setEstado(estado);

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
    public List<InscripcionCurso> listarInscripcionesEstudiante(String nickname) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            return em.createQuery(
                    "SELECT i FROM InscripcionCurso i "
                    + "JOIN FETCH i.edicionCurso ed "
                    + "JOIN FETCH ed.curso "
                    + "WHERE i.estudiante.nickname = :nickname",
                    InscripcionCurso.class
            ).setParameter("nickname", nickname).getResultList();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<InscripcionCurso> listarInscripcionesEdicion(String nombreEdicion) {

        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            return em.createQuery(
                    "SELECT i FROM InscripcionCurso i "
                    + "JOIN FETCH i.estudiante "
                    + "JOIN FETCH i.edicionCurso ed "
                    + "JOIN FETCH ed.curso "
                    + "WHERE ed.nombre = :nombreEdicion "
                    + "ORDER BY i.fechaInscripcion",
                    InscripcionCurso.class
            )
            .setParameter("nombreEdicion", nombreEdicion)
            .getResultList();

        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean altaCategoria(Categoria categoria){
        if (categoria == null || buscarCategoria(categoria.getNombre()) != null) {
            return false;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(categoria);
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
    public Categoria buscarCategoria(String nombre){
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            return em.find(Categoria.class, nombre);
        } finally {
            em.close();
        }
    }
}