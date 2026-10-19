package Logica;

import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CursoTest {

    @Test
    void constructorYGetters() {
        Date fecha = new Date();
        Curso curso = new Curso("Java","Curso de programación","3 meses",40,8,fecha,"https://ejemplo.com");

        assertEquals("Java", curso.getNombre());
        assertEquals("Curso de programación", curso.getDescripcion());
        assertEquals("3 meses", curso.getDuracion());
        assertEquals(40, curso.getCantHoras());
        assertEquals(8, curso.getCantCreditos());
        assertSame(fecha, curso.getFecRegistro());
        assertEquals("https://ejemplo.com", curso.getUrl());
    }
    
    @Test
    void settersActualizanLosDatos() {
        Curso curso = new Curso();
        java.util.Date fecha = new java.util.Date();
        Instituto instituto = new Instituto();

        java.util.List<EdicionCurso> ediciones = new java.util.ArrayList<>();
        java.util.List<ProgramaFormacion> programas = new java.util.ArrayList<>();
        java.util.List<Curso> previas = new java.util.ArrayList<>();

        curso.setDescripcion("Introduccion a Java");
        curso.setDuracion("6 meses");
        curso.setCantHoras(120);
        curso.setCantCreditos(15);
        curso.setFecRegistro(fecha);
        curso.setUrl("https://ejemplo.com/curso");
        curso.setInstituto(instituto);
        curso.setEdiciones(ediciones);
        curso.setProgramas(programas);
        curso.setPrevias(previas);

        assertEquals("Introduccion a Java", curso.getDescripcion());
        assertEquals("6 meses", curso.getDuracion());
        assertEquals(120, curso.getCantHoras());
        assertEquals(15, curso.getCantCreditos());
        assertEquals(fecha, curso.getFecRegistro());
        assertEquals("https://ejemplo.com/curso", curso.getUrl());
        assertSame(instituto, curso.getInstituto());
        assertSame(ediciones, curso.getEdiciones());
        assertSame(programas, curso.getProgramas());
        assertSame(previas, curso.getPrevias());
    }

    @Test
    void asociarInstituto() {
        Curso curso = new Curso();
        curso.setNombre("Java");

        Instituto instituto = new Instituto();

        curso.asociarInstituto(instituto);

        assertSame(instituto, curso.getInstituto());
        assertTrue(instituto.getCursos().contains(curso));
    }

    @Test
    void asociarInstitutoNoDuplicaCurso() {
        Curso curso = new Curso();
        curso.setNombre("Java");

        Instituto instituto = new Instituto();

        curso.asociarInstituto(instituto);
        curso.asociarInstituto(instituto);

        assertEquals(1, instituto.getCursos().size());
    }

    @Test
    void asociarInstitutoNulo() {
        Curso curso = new Curso();

        curso.asociarInstituto(null);
        assertNull(curso.getInstituto());
    }

    @Test
    void agregarYEliminarEdicion() {
        Curso curso = new Curso();
        EdicionCurso edicion = new EdicionCurso();
        edicion.setNombre("Java-2026");
        
        curso.agregarEdicion(edicion);
        assertTrue(curso.getEdiciones().contains(edicion));

        curso.eliminarEdicion(edicion);
        assertFalse(curso.getEdiciones().contains(edicion));
    }

    @Test
    void agregarEdicionNoDuplicaNiAceptaNulos() {
        Curso curso = new Curso();
        EdicionCurso edicion1 = new EdicionCurso();
        edicion1.setNombre("Java-2026");

        EdicionCurso edicion2 = new EdicionCurso();
        edicion2.setNombre("Java-2026");

        curso.agregarEdicion(edicion1);
        curso.agregarEdicion(edicion2);
        curso.agregarEdicion(null);
        assertEquals(1, curso.getEdiciones().size());
    }

    @Test
    void eliminarEdicionNulaNoModificaLaLista() {
        Curso curso = new Curso();
        curso.eliminarEdicion(null);

        assertTrue(curso.getEdiciones().isEmpty());
    }

    @Test
    void agregarYEliminarPrograma() {
        Curso curso = new Curso();
        ProgramaFormacion programa = new ProgramaFormacion();

        curso.agregarPrograma(programa);
        assertTrue(curso.getProgramas().contains(programa));

        curso.eliminarPrograma(programa);
        assertFalse(curso.getProgramas().contains(programa));
    }

    @Test
    void agregarProgramaNoDuplicaNiAceptaNulos() {
        Curso curso = new Curso();
        ProgramaFormacion programa = new ProgramaFormacion();

        curso.agregarPrograma(programa);
        curso.agregarPrograma(programa);
        curso.agregarPrograma(null);

        assertEquals(1, curso.getProgramas().size());
    }

    @Test
    void eliminarProgramaNuloNoModificaLaLista() {
        Curso curso = new Curso();
        curso.eliminarPrograma(null);

        assertTrue(curso.getProgramas().isEmpty());
    }

    @Test
    void agregarYEliminarPrevia() {
        Curso curso = new Curso();
        Curso previa = new Curso();

        previa.setNombre("Matemática");

        curso.agregarPrevia(previa);
        assertTrue(curso.getPrevias().contains(previa));

        curso.eliminarPrevia(previa);
        assertFalse(curso.getPrevias().contains(previa));
    }

    @Test
    void agregarPreviaNoDuplicaNiAceptaNulos() {
        Curso curso = new Curso();
        Curso previa = new Curso();

        previa.setNombre("Matemática");

        curso.agregarPrevia(previa);
        curso.agregarPrevia(previa);
        curso.agregarPrevia(null);

        assertEquals(1, curso.getPrevias().size());
    }

    @Test
    void eliminarPreviaNulaNoModificaLaLista() {
        Curso curso = new Curso();
        curso.eliminarPrevia(null);

        assertTrue(curso.getPrevias().isEmpty());
    }

    @Test
    void cursosConElMismoNombreSonIguales() {
        Curso curso1 = new Curso();
        curso1.setNombre("Java");

        Curso curso2 = new Curso();
        curso2.setNombre("Java");

        assertEquals(curso1, curso2);
        assertEquals(curso1.hashCode(), curso2.hashCode());
    }

    @Test
    void cursosConNombresDiferentesNoSonIguales() {
        Curso curso1 = new Curso();
        curso1.setNombre("Java");

        Curso curso2 = new Curso();
        curso2.setNombre("Python");

        assertNotEquals(curso1, curso2);
    }

    @Test
    void cursoNoEsIgualAOtroTipoDeObjeto() {
        Curso curso = new Curso();
        curso.setNombre("Java");

        assertNotEquals(curso, "Java");
        assertNotEquals(curso, null);
    }

    @Test
    void cursosSinNombre() {
        Curso curso1 = new Curso();
        Curso curso2 = new Curso();

        assertEquals(curso1, curso2);
        assertEquals(0, curso1.hashCode());
    }

    @Test
    void toStringDevuelveElNombre() {
        Curso curso = new Curso();
        curso.setNombre("Java");

        assertEquals("Java", curso.toString());
    }
}
