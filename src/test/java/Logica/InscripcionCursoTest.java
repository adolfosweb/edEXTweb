package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class InscripcionCursoTest {

    @Test
    void constructorVacio() {
        InscripcionCurso inscripcion = new InscripcionCurso();

        assertNull(inscripcion.getId());
        assertNull(inscripcion.getFechaIns());
        assertNull(inscripcion.getEstudiante());
        assertNull(inscripcion.getCurso());
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void constructorConDatos() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNickname("alumno1");

        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        LocalDate fecha = LocalDate.of(2026, 3, 10);

        InscripcionCurso inscripcion = new InscripcionCurso(estudiante, fecha, edicion);

        assertSame(estudiante, inscripcion.getEstudiante());
        assertEquals(fecha, inscripcion.getFechaIns());
        assertSame(edicion, inscripcion.getCurso());
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void setFechaIns() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        LocalDate fecha = LocalDate.of(2026, 4, 15);

        inscripcion.setFechaIns(fecha);
        assertEquals(fecha, inscripcion.getFechaIns());
    }

    @Test
    void setEstudianteActualizaLaListaDelEstudiante() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        Estudiante estudiante = new Estudiante();
        estudiante.setNickname("alumno1");

        inscripcion.setEstudiante(estudiante);

        assertSame(estudiante, inscripcion.getEstudiante());
        assertTrue(estudiante.getCursos().contains(inscripcion));
    }

    @Test
    void setEstudianteNulo() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        Estudiante estudiante = new Estudiante();

        inscripcion.setEstudiante(estudiante);
        inscripcion.setEstudiante(null);
        
        assertNull(inscripcion.getEstudiante());
    }

    @Test
    void setCursoActualizaLaListaDeLaEdicion() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        inscripcion.setCurso(edicion);
        assertSame(edicion, inscripcion.getCurso());
        assertTrue(edicion.getInscriptos().contains(inscripcion));
    }

    @Test
    void setCursoNulo() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        inscripcion.setCurso(edicion);
        inscripcion.setCurso(null);
        assertNull(inscripcion.getCurso());
    }

    @Test
    void setEstado() {
        InscripcionCurso inscripcion = new InscripcionCurso();

        // Comprueba que se puede asignar null.
        inscripcion.setEstado(null);
        assertNull(inscripcion.getEstado());

        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void asignarEstudianteNoDuplicaLaInscripcion() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        Estudiante estudiante = new Estudiante();

        inscripcion.setEstudiante(estudiante);
        inscripcion.setEstudiante(estudiante);

        assertEquals(1, estudiante.getCursos().size());
    }

    @Test
    void asignarEdicionNoDuplicaLaInscripcion() {
        InscripcionCurso inscripcion = new InscripcionCurso();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");

        inscripcion.setCurso(edicion);
        inscripcion.setCurso(edicion);
        assertEquals(1, edicion.getInscriptos().size());
    }
}
