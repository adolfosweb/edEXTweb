package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class DocenteTest {

    @Test
    void constructorYDatosHeredados() {
        LocalDate fecha = LocalDate.of(1985, 4, 10);
        Docente docente = new Docente("profesor1","profesor@email.com","Ana","Gomez",fecha);

        assertEquals("profesor1", docente.getNickname());
        assertEquals("profesor@email.com", docente.getCorreoElectronico());
        assertEquals("Ana", docente.getNombre());
        assertEquals("Gomez", docente.getApellido());
        assertEquals(fecha, docente.getFecNac());
        assertNotNull(docente.getInstitutos());
        assertNotNull(docente.getEdiciones());
    }

    @Test
    void constructorVacioInicializaLasListas() {
        Docente docente = new Docente();

        assertNotNull(docente.getInstitutos());
        assertNotNull(docente.getEdiciones());
        assertTrue(docente.getInstitutos().isEmpty());
        assertTrue(docente.getEdiciones().isEmpty());
    }

    @Test
    void agregarInstitutoValido() {
        Docente docente = new Docente();
        Instituto instituto = new Instituto("Instituto Central");

        docente.agregarInstituto(instituto);

        assertEquals(1, docente.getInstitutos().size());
        assertTrue(docente.getInstitutos().contains(instituto));
    }

    @Test
    void agregarInstitutoDuplicado() {
        Docente docente = new Docente();
        Instituto instituto = new Instituto("Instituto Central");

        docente.agregarInstituto(instituto);
        docente.agregarInstituto(instituto);

        assertEquals(1, docente.getInstitutos().size());
    }

    @Test
    void agregarInstitutoNulo() {
        Docente docente = new Docente();

        docente.agregarInstituto(null);

        assertTrue(docente.getInstitutos().isEmpty());
    }

    @Test
    void eliminarInstitutoExistente() {
        Docente docente = new Docente();
        Instituto instituto = new Instituto("Instituto Central");
        docente.agregarInstituto(instituto);

        docente.eliminarInstituto(instituto);

        assertTrue(docente.getInstitutos().isEmpty());
    }

    @Test
    void eliminarInstitutoNuloNoModificaLaLista() {
        Docente docente = new Docente();
        Instituto instituto = new Instituto("Instituto Central");
        docente.agregarInstituto(instituto);

        docente.eliminarInstituto(null);

        assertEquals(1, docente.getInstitutos().size());
    }

    @Test
    void agregarEdicionValida() {
        Docente docente = new Docente();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        docente.agregarEdicion(edicion);

        assertEquals(1, docente.getEdiciones().size());
        assertTrue(docente.getEdiciones().contains(edicion));
    }

    @Test
    void agregarEdicionDuplicada() {
        Docente docente = new Docente();

        EdicionCurso edicion1 = new EdicionCurso();
        edicion1.setNombre("Java-2026");

        EdicionCurso edicion2 = new EdicionCurso();
        edicion2.setNombre("Java-2026");

        docente.agregarEdicion(edicion1);
        docente.agregarEdicion(edicion2);

        assertEquals(1, docente.getEdiciones().size());
    }

    @Test
    void agregarEdicionNula() {
        Docente docente = new Docente();

        docente.agregarEdicion(null);
        assertTrue(docente.getEdiciones().isEmpty());
    }

    @Test
    void eliminarEdicionExistente() {
        Docente docente = new Docente();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");
        docente.agregarEdicion(edicion);

        docente.eliminarEdicion(edicion);
        assertTrue(docente.getEdiciones().isEmpty());
    }

    @Test
    void eliminarEdicionNulaNoModificaLaLista() {
        Docente docente = new Docente();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");
        docente.agregarEdicion(edicion);

        docente.eliminarEdicion(null);
        assertEquals(1, docente.getEdiciones().size());
    }

    @Test
    void obtenerCursosDesdeLasEdiciones() {
        Docente docente = new Docente();

        Curso curso = new Curso();
        curso.setNombre("Java");

        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");
        edicion.setCurso(curso);

        docente.agregarEdicion(edicion);

        assertEquals(1, docente.getCursos().size());
        assertTrue(docente.getCursos().contains(curso));
    }

    @Test
    void obtenerCursosNoDuplicaElMismoCurso() {
        Docente docente = new Docente();

        Curso curso1 = new Curso();
        curso1.setNombre("Java");

        Curso curso2 = new Curso();
        curso2.setNombre("Java");

        EdicionCurso edicion1 = new EdicionCurso();
        edicion1.setNombre("Java-2026");
        edicion1.setCurso(curso1);

        EdicionCurso edicion2 = new EdicionCurso();
        edicion2.setNombre("Java-2027");
        edicion2.setCurso(curso2);

        docente.agregarEdicion(edicion1);
        docente.agregarEdicion(edicion2);

        assertEquals(1, docente.getCursos().size());
    }

    @Test
    void obtenerCursosIgnoraEdicionesSinCurso() {
        Docente docente = new Docente();

        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        docente.agregarEdicion(edicion);
        assertTrue(docente.getCursos().isEmpty());
    }

    @Test
    void obtenerCursosSinEdiciones() {
        Docente docente = new Docente();
        assertTrue(docente.getCursos().isEmpty());
    }
}
