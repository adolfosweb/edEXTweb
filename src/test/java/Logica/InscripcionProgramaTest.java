package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.time.LocalDate;

public class InscripcionProgramaTest {

    @Test
    void constructorVacio() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        assertNull(inscripcion.getId());
        assertNull(inscripcion.getFechaIns());
        assertNull(inscripcion.getEstudiante());
        assertNull(inscripcion.getPrograma());
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void constructorConDatos() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNickname("alumno1");

        ProgramaFormacion programa = new ProgramaFormacion();
        programa.setNombre("Java");

        LocalDate fecha = LocalDate.of(2026, 3, 15);

        InscripcionPrograma inscripcion = new InscripcionPrograma(estudiante, fecha, programa);

        assertSame(estudiante, inscripcion.getEstudiante());
        assertEquals(fecha, inscripcion.getFechaIns());
        assertSame(programa, inscripcion.getPrograma());
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void setFechaIns() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        LocalDate fecha = LocalDate.of(2026, 5, 10);

        inscripcion.setFechaIns(fecha);
        assertEquals(fecha, inscripcion.getFechaIns());
    }

    @Test
    void setEstudiante() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        Estudiante estudiante = new Estudiante();
        estudiante.setNickname("alumno1");

        inscripcion.setEstudiante(estudiante);
        assertSame(estudiante, inscripcion.getEstudiante());
    }

    @Test
    void setEstudianteNulo() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        Estudiante estudiante = new Estudiante();

        inscripcion.setEstudiante(estudiante);
        inscripcion.setEstudiante(null);
        assertNull(inscripcion.getEstudiante());
    }

    @Test
    void setPrograma() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        ProgramaFormacion programa = new ProgramaFormacion();
        programa.setNombre("Java");

        inscripcion.setPrograma(programa);
        assertSame(programa, inscripcion.getPrograma());
    }

    @Test
    void setProgramaNulo() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        ProgramaFormacion programa = new ProgramaFormacion();

        inscripcion.setPrograma(programa);
        inscripcion.setPrograma(null);
        assertNull(inscripcion.getPrograma());
    }

    @Test
    void setEstado() {
        InscripcionPrograma inscripcion = new InscripcionPrograma();

        inscripcion.setEstado(null);
        assertNull(inscripcion.getEstado());

        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);
        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void callbackPrePersistInicializaEstadoNulo() throws Exception {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        inscripcion.setEstado(null);

        Method metodo = InscripcionPrograma.class.getDeclaredMethod("asignarEstadoInicial");
        metodo.setAccessible(true);
        metodo.invoke(inscripcion);

        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }

    @Test
    void callbackPrePersistConservaEstadoExistente() throws Exception {
        InscripcionPrograma inscripcion = new InscripcionPrograma();
        inscripcion.setEstado(EstadoInscripcion.INSCRIPTO);

        Method metodo = InscripcionPrograma.class.getDeclaredMethod("asignarEstadoInicial");
        metodo.setAccessible(true);
        metodo.invoke(inscripcion);

        assertEquals(EstadoInscripcion.INSCRIPTO, inscripcion.getEstado());
    }
}
