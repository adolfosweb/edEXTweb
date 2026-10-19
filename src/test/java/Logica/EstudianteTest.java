package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class EstudianteTest {

    @Test
    void constructorYDatosHeredados() {
        LocalDate fecha = LocalDate.of(2002, 8, 20);

        Estudiante estudiante = new Estudiante("estudiante1","estudiante@email.com","Pedro","Rodriguez",fecha);

        assertEquals("estudiante1", estudiante.getNickname());
        assertEquals("estudiante@email.com", estudiante.getCorreoElectronico());
        assertEquals("Pedro", estudiante.getNombre());
        assertEquals("Rodriguez", estudiante.getApellido());
        assertEquals(fecha, estudiante.getFecNac());
        assertNotNull(estudiante.getCursos());
        assertNotNull(estudiante.getProgramas());
    }

    @Test
    void constructorVacioInicializaLasListas() {
        Estudiante estudiante = new Estudiante();

        assertNotNull(estudiante.getCursos());
        assertNotNull(estudiante.getProgramas());
        assertTrue(estudiante.getCursos().isEmpty());
        assertTrue(estudiante.getProgramas().isEmpty());
    }

    @Test
    void agregarInscripcionCurso() {
        Estudiante estudiante = new Estudiante();
        InscripcionCurso inscripcion = new InscripcionCurso();

        estudiante.agregarInscripcionCurso(inscripcion);

        assertEquals(1, estudiante.getCursos().size());
        assertTrue(estudiante.getCursos().contains(inscripcion));
        assertSame(estudiante, inscripcion.getEstudiante());
    }

    @Test
    void agregarInscripcionCursoNula() {
        Estudiante estudiante = new Estudiante();

        estudiante.agregarInscripcionCurso(null);
        assertTrue(estudiante.getCursos().isEmpty());
    }

    @Test
    void agregarInscripcionCursoDuplicada() {
        Estudiante estudiante = new Estudiante();
        InscripcionCurso inscripcion = new InscripcionCurso();

        estudiante.agregarInscripcionCurso(inscripcion);
        estudiante.agregarInscripcionCurso(inscripcion);
        assertEquals(1, estudiante.getCursos().size());
    }

    @Test
    void eliminarInscripcionCurso() {
        Estudiante estudiante = new Estudiante();
        InscripcionCurso inscripcion = new InscripcionCurso();
        estudiante.agregarInscripcionCurso(inscripcion);

        estudiante.eliminarInscripcionCurso(inscripcion);

        assertTrue(estudiante.getCursos().isEmpty());
        assertNull(inscripcion.getEstudiante());
    }

    @Test
    void eliminarInscripcionCursoNula() {
        Estudiante estudiante = new Estudiante();
        InscripcionCurso inscripcion = new InscripcionCurso();
        estudiante.agregarInscripcionCurso(inscripcion);

        estudiante.eliminarInscripcionCurso(null);

        assertEquals(1, estudiante.getCursos().size());
        assertSame(estudiante, inscripcion.getEstudiante());
    }

    @Test
    void eliminarInscripcionCursoInexistente() {
        Estudiante estudiante = new Estudiante();
        InscripcionCurso inscripcion = new InscripcionCurso();

        estudiante.eliminarInscripcionCurso(inscripcion);
        assertTrue(estudiante.getCursos().isEmpty());
    }

    @Test
    void agregarInscripcionPrograma() {
        Estudiante estudiante = new Estudiante();
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        estudiante.agregarInscripcionPrograma(inscripcion);

        assertEquals(1, estudiante.getProgramas().size());
        assertTrue(estudiante.getProgramas().contains(inscripcion));
        assertSame(estudiante, inscripcion.getEstudiante());
    }

    @Test
    void agregarInscripcionProgramaNula() {
        Estudiante estudiante = new Estudiante();

        estudiante.agregarInscripcionPrograma(null);
        assertTrue(estudiante.getProgramas().isEmpty());
    }

    @Test
    void agregarInscripcionProgramaDuplicada() {
        Estudiante estudiante = new Estudiante();
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        estudiante.agregarInscripcionPrograma(inscripcion);
        estudiante.agregarInscripcionPrograma(inscripcion);
        assertEquals(1, estudiante.getProgramas().size());
    }

    @Test
    void eliminarInscripcionPrograma() {
        Estudiante estudiante = new Estudiante();
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        estudiante.agregarInscripcionPrograma(inscripcion);

        estudiante.eliminarInscripcionPrograma(inscripcion);

        assertTrue(estudiante.getProgramas().isEmpty());
        assertNull(inscripcion.getEstudiante());
    }

    @Test
    void eliminarInscripcionProgramaNula() {
        Estudiante estudiante = new Estudiante();
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        estudiante.agregarInscripcionPrograma(inscripcion);

        estudiante.eliminarInscripcionPrograma(null);

        assertEquals(1, estudiante.getProgramas().size());
        assertSame(estudiante, inscripcion.getEstudiante());
    }

    @Test
    void eliminarInscripcionProgramaInexistente() {
        Estudiante estudiante = new Estudiante();
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        estudiante.eliminarInscripcionPrograma(inscripcion);
        assertTrue(estudiante.getProgramas().isEmpty());
    }
}
