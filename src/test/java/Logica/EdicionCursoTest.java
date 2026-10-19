package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class EdicionCursoTest {

    @Test
    void constructorYGetters() {
        LocalDate inicio = LocalDate.of(2026, 3, 1);
        LocalDate fin = LocalDate.of(2026, 7, 1);
        LocalDate publicacion = LocalDate.of(2026, 2, 1);

        Curso curso = new Curso();
        curso.setNombre("Java");

        Docente docente = new Docente();
        docente.setNickname("profesor1");

        EdicionCurso edicion = new EdicionCurso("Java-2026",inicio,fin,30,publicacion,curso,docente);

        assertEquals("Java-2026", edicion.getNombre());
        assertEquals(inicio, edicion.getFechaInicio());
        assertEquals(fin, edicion.getFechaFin());
        assertEquals(30, edicion.getCupo());
        assertEquals(publicacion, edicion.getFechaPublicacion());
        assertSame(curso, edicion.getCurso());
        assertTrue(edicion.getDocentes().contains(docente));
        assertNotNull(edicion.getInscriptos());
    }

    @Test
    void constructorVacioInicializaLasListas() {
        EdicionCurso edicion = new EdicionCurso();

        assertNotNull(edicion.getDocentes());
        assertNotNull(edicion.getInscriptos());
        assertTrue(edicion.getDocentes().isEmpty());
        assertTrue(edicion.getInscriptos().isEmpty());
    }

    @Test
    void constructorAceptaDocenteNulo() {
        EdicionCurso edicion = new EdicionCurso("Java-2026",null,null,20,null,null,null);

        assertTrue(edicion.getDocentes().isEmpty());
        assertNull(edicion.getCurso());
    }

    @Test
    void settersActualizanLosDatos() {
        EdicionCurso edicion = new EdicionCurso();

        LocalDate inicio = LocalDate.of(2026, 4, 1);
        LocalDate fin = LocalDate.of(2026, 8, 1);
        LocalDate publicacion = LocalDate.of(2026, 3, 1);

        edicion.setNombre("Python-2026");
        edicion.setFechaInicio(inicio);
        edicion.setFechaFin(fin);
        edicion.setCupo(25);
        edicion.setFechaPublicacion(publicacion);

        assertEquals("Python-2026", edicion.getNombre());
        assertEquals(inicio, edicion.getFechaInicio());
        assertEquals(fin, edicion.getFechaFin());
        assertEquals(25, edicion.getCupo());
        assertEquals(publicacion, edicion.getFechaPublicacion());
    }

    @Test
    void asignarCursoActualizaLaRelacionInversa() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        Curso curso = new Curso();
        curso.setNombre("Java");

        edicion.setCurso(curso);

        assertSame(curso, edicion.getCurso());
        assertTrue(curso.getEdiciones().contains(edicion));
    }

    @Test
    void asignarCursoNoDuplicaLaEdicion() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        Curso curso = new Curso();
        curso.setNombre("Java");

        edicion.setCurso(curso);
        edicion.setCurso(curso);
        assertEquals(1, curso.getEdiciones().size());
    }

    @Test
    void asignarCursoNulo() {
        EdicionCurso edicion = new EdicionCurso();

        edicion.setCurso(null);
        assertNull(edicion.getCurso());
    }

    @Test
    void agregarDocenteActualizaAmbosLados() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        Docente docente = new Docente();
        docente.setNickname("profesor1");

        edicion.agregarDocente(docente);
        assertTrue(edicion.getDocentes().contains(docente));
        assertTrue(docente.getEdiciones().contains(edicion));
    }

    @Test
    void agregarDocenteNoDuplicaLaRelacion() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        Docente docente = new Docente();
        docente.setNickname("profesor1");

        edicion.agregarDocente(docente);
        edicion.agregarDocente(docente);

        assertEquals(1, edicion.getDocentes().size());
        assertEquals(1, docente.getEdiciones().size());
    }

    @Test
    void agregarDocenteNulo() {
        EdicionCurso edicion = new EdicionCurso();

        edicion.agregarDocente(null);
        assertTrue(edicion.getDocentes().isEmpty());
    }

    @Test
    void eliminarDocenteActualizaAmbosLados() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        Docente docente = new Docente();
        docente.setNickname("profesor1");

        edicion.agregarDocente(docente);
        edicion.eliminarDocente(docente);

        assertFalse(edicion.getDocentes().contains(docente));
        assertFalse(docente.getEdiciones().contains(edicion));
    }

    @Test
    void eliminarDocenteNuloNoModificaLaLista() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        Docente docente = new Docente();
        docente.setNickname("profesor1");

        edicion.agregarDocente(docente);
        edicion.eliminarDocente(null);
        assertEquals(1, edicion.getDocentes().size());
    }

    @Test
    void agregarInscriptoActualizaLaRelacionInversa() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        InscripcionCurso inscripcion = new InscripcionCurso();

        edicion.agregarInscripto(inscripcion);

        assertTrue(edicion.getInscriptos().contains(inscripcion));
        assertSame(edicion, inscripcion.getCurso());
    }

    @Test
    void agregarInscriptoNoDuplicaLaInscripcion() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        InscripcionCurso inscripcion = new InscripcionCurso();

        edicion.agregarInscripto(inscripcion);
        edicion.agregarInscripto(inscripcion);
        assertEquals(1, edicion.getInscriptos().size());
    }

    @Test
    void agregarInscriptoNulo() {
        EdicionCurso edicion = new EdicionCurso();

        edicion.agregarInscripto(null);
        assertTrue(edicion.getInscriptos().isEmpty());
    }

    @Test
    void eliminarInscriptoActualizaLaRelacionInversa() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        InscripcionCurso inscripcion = new InscripcionCurso();
        edicion.agregarInscripto(inscripcion);

        edicion.eliminarInscripto(inscripcion);
        assertFalse(edicion.getInscriptos().contains(inscripcion));
        assertNull(inscripcion.getCurso());
    }

    @Test
    void eliminarInscriptoNuloNoModificaLaLista() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        InscripcionCurso inscripcion = new InscripcionCurso();
        edicion.agregarInscripto(inscripcion);

        edicion.eliminarInscripto(null);
        assertEquals(1, edicion.getInscriptos().size());
    }

    @Test
    void edicionesConElMismoNombreSonIguales() {
        EdicionCurso edicion1 = new EdicionCurso();
        edicion1.setNombre("Java-2026");

        EdicionCurso edicion2 = new EdicionCurso();
        edicion2.setNombre("Java-2026");

        assertEquals(edicion1, edicion2);
        assertEquals(edicion1.hashCode(), edicion2.hashCode());
    }

    @Test
    void edicionesConNombresDiferentesNoSonIguales() {
        EdicionCurso edicion1 = new EdicionCurso();
        edicion1.setNombre("Java-2026");

        EdicionCurso edicion2 = new EdicionCurso();
        edicion2.setNombre("Java-2027");

        assertNotEquals(edicion1, edicion2);
    }

    @Test
    void edicionSinNombreNoEsIgualAOtraEdicionSinNombre() {
        EdicionCurso edicion1 = new EdicionCurso();
        EdicionCurso edicion2 = new EdicionCurso();

        assertNotEquals(edicion1, edicion2);
        assertEquals(0, edicion1.hashCode());
    }

    @Test
    void edicionNoEsIgualAOtroTipoDeObjeto() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        assertNotEquals(edicion, "Java-2026");
        assertNotEquals(edicion, null);
    }

    @Test
    void toStringDevuelveElNombre() {
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");
        assertEquals("Java-2026", edicion.toString());
    }
}
