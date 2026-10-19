package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    @Test
    void categoriaNuevaTieneNombreNulo() {
        Categoria categoria = new Categoria();
        assertNull(categoria.getNombre());
    }

    @Test
    void setIdYGetNombre() {
        Categoria categoria = new Categoria();

        categoria.setId("Programacion");
        assertEquals("Programacion", categoria.getNombre());
    }

    @Test
    void categoriasConElMismoNombreSonIguales() {
        Categoria categoria1 = new Categoria();
        categoria1.setId("Programacion");

        Categoria categoria2 = new Categoria();
        categoria2.setId("Programacion");

        assertEquals(categoria1, categoria2);
        assertEquals(categoria1.hashCode(), categoria2.hashCode());
    }

    @Test
    void categoriasConNombresDiferentesNoSonIguales() {
        Categoria categoria1 = new Categoria();
        categoria1.setId("Programacion");

        Categoria categoria2 = new Categoria();
        categoria2.setId("Matematica");

        assertNotEquals(categoria1, categoria2);
    }

    @Test
    void categoriasSinNombreSonIgualesSegunLaImplementacionActual() {
        Categoria categoria1 = new Categoria();
        Categoria categoria2 = new Categoria();

        assertEquals(categoria1, categoria2);
        assertEquals(0, categoria1.hashCode());
        assertEquals(0, categoria2.hashCode());
    }

    @Test
    void categoriaNoEsIgualAOtroTipoDeObjeto() {
        Categoria categoria = new Categoria();
        categoria.setId("Programacion");

        assertNotEquals(categoria, "Programacion");
        assertNotEquals(categoria, null);
    }

    @Test
    void toStringIncluyeElNombre() {
        Categoria categoria = new Categoria();
        categoria.setId("Programacion");
        
        assertEquals("Logica.Categoria[ id=Programacion ]",categoria.toString());
    }
}
