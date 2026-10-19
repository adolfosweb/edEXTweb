package Logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UsuarioTest {

    @Test
    void constructorYGetters() {
        LocalDate fecha = LocalDate.of(2000, 5, 15);
        Usuario usuario = new Usuario("juan","juan@email.com","Juan","Perez",fecha);

        assertEquals("juan", usuario.getNickname());
        assertEquals("juan@email.com", usuario.getCorreoElectronico());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals(fecha, usuario.getFecNac());
    }

    @Test
    void constructorVacio() {
        Usuario usuario = new Usuario();

        assertNull(usuario.getNickname());
        assertNull(usuario.getCorreoElectronico());
        assertNull(usuario.getNombre());
        assertNull(usuario.getApellido());
        assertNull(usuario.getFecNac());
        assertNull(usuario.getPassword());
    }

    @Test
    void settersActualizanLosDatos() {
        Usuario usuario = new Usuario();
        LocalDate fecha = LocalDate.of(1999, 10, 20);

        usuario.setNickname("maria");
        usuario.setCorreoElectronico("maria@email.com");
        usuario.setNombre("Maria");
        usuario.setApellido("Lopez");
        usuario.setFecNac(fecha);
        usuario.setPassword("clave123");

        assertEquals("maria", usuario.getNickname());
        assertEquals("maria@email.com", usuario.getCorreoElectronico());
        assertEquals("Maria", usuario.getNombre());
        assertEquals("Lopez", usuario.getApellido());
        assertEquals(fecha, usuario.getFecNac());
        assertEquals("clave123", usuario.getPassword());
    }

    @Test
    void cambiarPassword() {
        Usuario usuario = new Usuario();

        usuario.setPassword("clave1");
        assertEquals("clave1", usuario.getPassword());

        usuario.setPassword("clave2");
        assertEquals("clave2", usuario.getPassword());
    }

    @Test
    void usuariosConElMismoNicknameSonIguales() {
        Usuario usuario1 = new Usuario();
        usuario1.setNickname("juan");

        Usuario usuario2 = new Usuario();
        usuario2.setNickname("juan");

        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void usuariosConNicknameDiferenteNoSonIguales() {
        Usuario usuario1 = new Usuario();
        usuario1.setNickname("juan");

        Usuario usuario2 = new Usuario();
        usuario2.setNickname("maria");

        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void usuarioNoEsIgualAOtroTipoDeObjeto() {
        Usuario usuario = new Usuario();
        usuario.setNickname("juan");

        assertNotEquals(usuario, "juan");
        assertNotEquals(usuario, null);
    }

    @Test
    void usuariosSinNicknameSonIgualesSegunLaImplementacionActual() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();

        assertEquals(usuario1, usuario2);
        assertEquals(0, usuario1.hashCode());
        assertEquals(0, usuario2.hashCode());
    }

    @Test
    void usuariosDeDistintasSubclasesPuedenSerIguales() {
        Usuario usuario = new Usuario();
        usuario.setNickname("juan");

        Docente docente = new Docente();
        docente.setNickname("juan");

        assertEquals(usuario, docente);
        assertEquals(docente, usuario);
    }
}
