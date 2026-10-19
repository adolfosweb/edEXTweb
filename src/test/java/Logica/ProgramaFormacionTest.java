package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class ProgramaFormacionTest {

    @Test
    void constructorYGetters() {
        ProgramaFormacion programa = new ProgramaFormacion("Java","Programa de formación en Java",LocalDate.of(2026, 3, 1),LocalDate.of(2026, 12, 1));

        assertEquals("Java", programa.getNombre());
        assertEquals("Programa de formación en Java", programa.getDescripcion());
        assertEquals(LocalDate.of(2026, 3, 1), programa.getFechaInicio());
        assertEquals(LocalDate.of(2026, 12, 1), programa.getFechaFin());
        assertNotNull(programa.getCursos());
        assertNotNull(programa.getInscriptos());
        assertTrue(programa.getCursos().isEmpty());
        assertTrue(programa.getInscriptos().isEmpty());
    }
    
    @Test
    void constructorParametros2YGetters() {
        LocalDate fechaInicio = LocalDate.of(2026, 3, 1);
        LocalDate fechaFin = LocalDate.of(2026, 11, 30);

        ProgramaFormacion programa = new ProgramaFormacion("Java","Programa de formacion en Java","java.png",fechaInicio,fechaFin);

        assertEquals("Java", programa.getNombre());
        assertEquals("Programa de formacion en Java", programa.getDescripcion());
        assertEquals("java.png", programa.getImagen());
        assertEquals(fechaInicio, programa.getFechaInicio());
        assertEquals(fechaFin, programa.getFechaFin());
    }

    @Test
    void settersActualizanLosDatos() {
        ProgramaFormacion programa = new ProgramaFormacion();

        LocalDate fechaInicio = LocalDate.of(2026, 3, 1);
        LocalDate fechaFin = LocalDate.of(2026, 11, 30);
        LocalDate fechaAlta = LocalDate.of(2026, 2, 15);

        programa.setDescripcion("Nueva descripcion");
        programa.setImagen("programa.png");
        programa.setFechaInicio(fechaInicio);
        programa.setFechaFin(fechaFin);
        programa.setFechaAlta(fechaAlta);

        assertEquals("Nueva descripcion", programa.getDescripcion());
        assertEquals("programa.png", programa.getImagen());
        assertEquals(fechaInicio, programa.getFechaInicio());
        assertEquals(fechaFin, programa.getFechaFin());
        assertEquals(fechaAlta, programa.getFechaAlta());
    }
    
    @Test
    void constructorVacioInicializaLasListas() {
        ProgramaFormacion programa = new ProgramaFormacion();

        assertNotNull(programa.getCursos());
        assertNotNull(programa.getInscriptos());
        assertTrue(programa.getCursos().isEmpty());
        assertTrue(programa.getInscriptos().isEmpty());
    }

    @Test
    void agregarCursoValido() {
        ProgramaFormacion programa = new ProgramaFormacion();
        Curso curso = new Curso();
        curso.setNombre("Java");

        boolean resultado = programa.agregarCurso(curso);

        assertTrue(resultado);
        assertEquals(1, programa.getCursos().size());
        assertTrue(programa.getCursos().contains(curso));
    }

    @Test
    void agregarCursoDuplicado() {
        ProgramaFormacion programa = new ProgramaFormacion();
        Curso curso1 = new Curso();
        curso1.setNombre("Java");

        Curso curso2 = new Curso();
        curso2.setNombre("Java");

        programa.agregarCurso(curso1);
        boolean resultado = programa.agregarCurso(curso2);

        assertFalse(resultado);
        assertEquals(1, programa.getCursos().size());
    }

    @Test
    void agregarCursoNulo() {
        ProgramaFormacion programa = new ProgramaFormacion();

        boolean resultado = programa.agregarCurso(null);

        assertFalse(resultado);
        assertTrue(programa.getCursos().isEmpty());
    }

    @Test
    void eliminarCursoExistente() {
        ProgramaFormacion programa = new ProgramaFormacion();
        Curso curso = new Curso();
        curso.setNombre("Java");
        programa.agregarCurso(curso);

        programa.eliminarCurso(curso);

        assertTrue(programa.getCursos().isEmpty());
    }

    @Test
    void eliminarCursoInexistente() {
        ProgramaFormacion programa = new ProgramaFormacion();
        Curso curso = new Curso();
        curso.setNombre("Java");

        programa.eliminarCurso(curso);

        assertTrue(programa.getCursos().isEmpty());
    }

    @Test
    void eliminarCursoNulo() {
        ProgramaFormacion programa = new ProgramaFormacion();
        Curso curso = new Curso();
        curso.setNombre("Java");
        programa.agregarCurso(curso);

        programa.eliminarCurso(null);

        assertEquals(1, programa.getCursos().size());
    }

    @Test
    void agregarInscriptoValido() {
        ProgramaFormacion programa = new ProgramaFormacion();
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        programa.agregarInscripto(inscripcion);

        assertEquals(1, programa.getInscriptos().size());
        assertTrue(programa.getInscriptos().contains(inscripcion));
    }

    @Test
    void agregarInscriptoNulo() {
        ProgramaFormacion programa = new ProgramaFormacion();

        programa.agregarInscripto(null);
        assertTrue(programa.getInscriptos().isEmpty());
    }

    @Test
    void agregarInscriptoDuplicado() {
        ProgramaFormacion programa = new ProgramaFormacion();
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        programa.agregarInscripto(inscripcion);
        programa.agregarInscripto(inscripcion);
        assertEquals(1, programa.getInscriptos().size());
    }

    @Test
    void eliminarInscriptoExistente() {
        ProgramaFormacion programa = new ProgramaFormacion();
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        programa.agregarInscripto(inscripcion);

        programa.eliminarInscripto(inscripcion);
        assertTrue(programa.getInscriptos().isEmpty());
    }

    @Test
    void eliminarInscriptoInexistente() {
        ProgramaFormacion programa = new ProgramaFormacion();
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        programa.eliminarInscripto(inscripcion);
        assertTrue(programa.getInscriptos().isEmpty());
    }

    @Test
    void eliminarInscriptoNulo() {
        ProgramaFormacion programa = new ProgramaFormacion();
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        programa.agregarInscripto(inscripcion);

        programa.eliminarInscripto(null);
        assertEquals(1, programa.getInscriptos().size());
    }

    @Test
    void programasConElMismoNombreSonIguales() {
        ProgramaFormacion programa1 = new ProgramaFormacion();
        programa1.setNombre("Java");

        ProgramaFormacion programa2 = new ProgramaFormacion();
        programa2.setNombre("Java");

        assertEquals(programa1, programa2);
        assertEquals(programa1.hashCode(), programa2.hashCode());
    }

    @Test
    void programasConNombresDiferentesNoSonIguales() {
        ProgramaFormacion programa1 = new ProgramaFormacion();
        programa1.setNombre("Java");

        ProgramaFormacion programa2 = new ProgramaFormacion();
        programa2.setNombre("Python");

        assertNotEquals(programa1, programa2);
    }

    @Test
    void programaNoEsIgualAOtroTipoDeObjeto() {
        ProgramaFormacion programa = new ProgramaFormacion();
        programa.setNombre("Java");

        assertNotEquals(programa, "Java");
        assertNotEquals(programa, null);
    }

    @Test
    void programasSinNombre() {
        ProgramaFormacion programa1 = new ProgramaFormacion();
        ProgramaFormacion programa2 = new ProgramaFormacion();

        assertEquals(programa1, programa2);
    }

    @Test
    void toStringDevuelveElNombre() {
        ProgramaFormacion programa = new ProgramaFormacion();
        programa.setNombre("Java");

        assertEquals("Java", programa.toString());
    }
}