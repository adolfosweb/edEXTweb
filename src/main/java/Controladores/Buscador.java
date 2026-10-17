package Controladores;

import DTO.ResultadoBusqueda;
import Logica.Curso;
import Logica.ProgramaFormacion;
import Logica.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Buscador {


    public List<ResultadoBusqueda> buscar(String query, String filter, String sort) {

        List<ResultadoBusqueda> resultados = new ArrayList<>();

        String texto = query == null ? "" : query.trim();
        String filtro = normalizarFiltro(filter);
        String orden = normalizarOrden(sort);

        EntityManager em = Conexion.getInstancia().getEntityManager();

        try {
            String coincidencia = "%" + texto.toLowerCase(Locale.ROOT) + "%";
            // Buscar cursos.
            if (filtro.equals("todos") || filtro.equals("curso")) {
                TypedQuery<Curso> consultaCursos;
                if (texto.isEmpty()) {
                    consultaCursos = em.createQuery("SELECT c FROM Curso c",Curso.class);
                } else {
                    consultaCursos = em.createQuery(
                        "SELECT c FROM Curso c "
                        + "WHERE LOWER(c.nombre) LIKE :texto "
                        + "OR LOWER(c.descripcion) LIKE :texto",
                        Curso.class
                    );
                    consultaCursos.setParameter("texto", coincidencia);
                }
                for (Curso curso : consultaCursos.getResultList()) {
                    resultados.add(new ResultadoBusqueda(
                        "curso",
                        curso.getNombre(),
                        curso.getNombre(),
                        curso.getDescripcion(),
                        convertirFecha(curso.getFecRegistro())
                    ));
                }
            }
            // Buscar programas.
            if (filtro.equals("todos") || filtro.equals("programa")) {
                TypedQuery<ProgramaFormacion> consultaProgramas;
                if (texto.isEmpty()) {
                    consultaProgramas = em.createQuery("SELECT p FROM ProgramaFormacion p",ProgramaFormacion.class);
                } else {
                    consultaProgramas = em.createQuery(
                        "SELECT p FROM ProgramaFormacion p "
                        + "WHERE LOWER(p.nombre) LIKE :texto "
                        + "OR LOWER(p.descripcion) LIKE :texto",
                        ProgramaFormacion.class
                    );
                    consultaProgramas.setParameter("texto", coincidencia);
                }
                for (ProgramaFormacion programa : consultaProgramas.getResultList()) {
                    resultados.add(new ResultadoBusqueda(
                        "programa",
                        programa.getNombre(),
                        programa.getNombre(),
                        programa.getDescripcion(),
                        programa.getFechaAlta()
                    ));
                }
            }
            // Buscar usuarios por nickname.
            if (filtro.equals("todos") || filtro.equals("usuario")) {
                TypedQuery<Usuario> consultaUsuarios;
                if (texto.isEmpty()) {
                    consultaUsuarios = em.createQuery("SELECT u FROM Usuario u",Usuario.class);
                } else {
                    consultaUsuarios = em.createQuery(
                        "SELECT u FROM Usuario u "
                        + "WHERE LOWER(u.nickname) LIKE :texto",
                        Usuario.class
                    );
                    consultaUsuarios.setParameter("texto", coincidencia);
                }
                for (Usuario usuario : consultaUsuarios.getResultList()) {
                    String nombreVisible = usuario.getNickname();
                    resultados.add(new ResultadoBusqueda(
                        "usuario",
                        usuario.getNickname(),
                        nombreVisible,
                        null,
                        null
                    ));
                }
            }
        } finally {
            em.close();
        }
        // Ordenar la lista combinada.
        if (orden.equals("fecha_desc")) {
            resultados.sort(
                Comparator.comparing((ResultadoBusqueda r) -> r.getTipo().equals("usuario"))
                .thenComparing(
                    ResultadoBusqueda::getFecha,
                    Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(
                    ResultadoBusqueda::getNombre,
                    String.CASE_INSENSITIVE_ORDER)
            );
        } else {
            resultados.sort(Comparator.comparing(ResultadoBusqueda::getNombre,String.CASE_INSENSITIVE_ORDER));
        }
        return resultados;
    }


    private String normalizarFiltro(String filtro) {
        if (filtro == null) {
            return "todos";
        }

        switch (filtro.toLowerCase(Locale.ROOT)) {
            case "curso":
            case "programa":
            case "usuario":
                return filtro.toLowerCase(Locale.ROOT);
            default:
                return "todos";
        }
    }

    private String normalizarOrden(String orden) {
        if ("fecha_desc".equalsIgnoreCase(orden)) {
            return "fecha_desc";
        }

        return "nombre_asc";
    }

    private LocalDate convertirFecha(Date fecha) {
        if (fecha == null) {
            return null;
        }

        if (fecha instanceof java.sql.Date) {
            return ((java.sql.Date) fecha).toLocalDate();
        }

        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

