
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "seguimiento",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"seguidor_nickname", "seguido_nickname"}
        )
    }
)
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seguidor_nickname", referencedColumnName = "nickname")
    private Usuario seguidor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seguido_nickname", referencedColumnName = "nickname")
    private Usuario seguido;

    public Seguimiento() {
    }

    public Seguimiento(Usuario seguidor, Usuario seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
    }

    public Long getId() {
        return id;
    }

    public Usuario getSeguidor() {
        return seguidor;
    }

    public void setSeguidor(Usuario seguidor) {
        this.seguidor = seguidor;
    }

    public Usuario getSeguido() {
        return seguido;
    }

    public void setSeguido(Usuario seguido) {
        this.seguido = seguido;
    }
}
