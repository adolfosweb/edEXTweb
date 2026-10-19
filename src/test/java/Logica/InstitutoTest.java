package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InstitutoTest {

    @Test
    void constructorYGetters() {
        Instituto instituto = new Instituto("Instituto Central");

        assertEquals("Instituto Central", instituto.getNombre());
        assertNotNull(instituto.getDocentes());
        assertNotNull(instituto.getCursos());
        assertTrue(instituto.getDocentes().isEmpty());
        assertTrue(instituto.getCursos().isEmpty());
    }

    @Test
    void constructorVacio() {
        Instituto instituto = new Instituto();

        assertNull(instituto.getNombre());
        assertNotNull(instituto.getDocentes());
        assertNotNull(instituto.getCursos());
    }

    @Test
    void setNombre() {
        Instituto instituto = new Instituto();

        instituto.setNombre("Instituto Tecnologico");
        assertEquals("Instituto Tecnologico", instituto.getNombre());
    }

    @Test
    void agregarDocente() {
        Instituto instituto = new Instituto("Central");
        Docente docente = new Docente();
        docente.setNickname("profesor1");

        instituto.agregarDocente(docente);
        assertEquals(1, instituto.getDocentes().size());
        assertTrue(instituto.getDocentes().contains(docente));
    }

    @Test
    void agregarDocenteNulo() {
        Instituto instituto = new Instituto("Central");

        instituto.agregarDocente(null);
        assertTrue(instituto.getDocentes().isEmpty());
    }

    @Test
    void agregarDocenteDuplicado() {
        Instituto instituto = new Instituto("Central");
        Docente docente = new Docente();
        docente.setNickname("profesor1");

        instituto.agregarDocente(docente);
        instituto.agregarDocente(docente);

        // La implementación actual permite duplicados.
        assertEquals(2, instituto.getDocentes().size());
    }

    @Test
    void eliminarDocente() {
        Instituto instituto = new Instituto("Central");
        Docente docente = new Docente();
        docente.setNickname("profesor1");
        instituto.agregarDocente(docente);

        instituto.eliminarDocente(docente);
        assertTrue(instituto.getDocentes().isEmpty());
    }

    @Test
    void eliminarDocenteNulo() {
        Instituto instituto = new Instituto("Central");
        Docente docente = new Docente();
        docente.setNickname("profesor1");
        instituto.agregarDocente(docente);

        instituto.eliminarDocente(null);
        assertEquals(1, instituto.getDocentes().size());
    }

    @Test
    void eliminarDocenteInexistente() {
        Instituto instituto = new Instituto("Central");
        Docente docente = new Docente();
        docente.setNickname("profesor1");

        instituto.eliminarDocente(docente);
        assertTrue(instituto.getDocentes().isEmpty());
    }

    @Test
    void agregarCurso() {
        Instituto instituto = new Instituto("Central");
        Curso curso = new Curso();
        curso.setNombre("Java");

        instituto.agregarCurso(curso);

        assertEquals(1, instituto.getCursos().size());
        assertTrue(instituto.getCursos().contains(curso));
    }

    @Test
    void agregarCursoNulo() {
        Instituto instituto = new Instituto("Central");

        instituto.agregarCurso(null);
        assertTrue(instituto.getCursos().isEmpty());
    }

    @Test
    void agregarCursoDuplicado() {
        Instituto instituto = new Instituto("Central");
        Curso curso = new Curso();
        curso.setNombre("Java");

        instituto.agregarCurso(curso);
        instituto.agregarCurso(curso);

        // La implementación actual permite duplicados.
        assertEquals(2, instituto.getCursos().size());
    }

    @Test
    void eliminarCurso() {
        Instituto instituto = new Instituto("Central");
        Curso curso = new Curso();
        curso.setNombre("Java");
        instituto.agregarCurso(curso);

        instituto.eliminarCurso(curso);
        assertTrue(instituto.getCursos().isEmpty());
    }

    @Test
    void eliminarCursoNulo() {
        Instituto instituto = new Instituto("Central");
        Curso curso = new Curso();
        curso.setNombre("Java");
        instituto.agregarCurso(curso);

        instituto.eliminarCurso(null);
        assertEquals(1, instituto.getCursos().size());
    }

    @Test
    void institutosConElMismoNombreSonIguales() {
        Instituto i1 = new Instituto("Central");
        Instituto i2 = new Instituto("Central");

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @Test
    void institutosConNombresDiferentesNoSonIguales() {
        Instituto i1 = new Instituto("Central");
        Instituto i2 = new Instituto("Norte");

        assertNotEquals(i1, i2);
    }

    @Test
    void institutosSinNombreSonIgualesSegunLaImplementacionActual() {
        Instituto i1 = new Instituto();
        Instituto i2 = new Instituto();

        assertEquals(i1, i2);
        assertEquals(0, i1.hashCode());
    }

    @Test
    void institutoNoEsIgualAOtroTipoDeObjeto() {
        Instituto instituto = new Instituto("Central");

        assertNotEquals(instituto, "Central");
        assertNotEquals(instituto, null);
    }

    @Test
    void toStringDevuelveElNombre() {
        Instituto instituto = new Instituto("Central");

        assertEquals("Central", instituto.toString());
    }
}