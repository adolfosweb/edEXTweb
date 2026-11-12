package Datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UsuarioDTO")
public class UsuarioDTO {

    private String nickname;
    private String correoElectronico;
    private String nombre;
    private String apellido;
    private String fecNac;
    private String tipoUsuario;
    private String imagen;   // ruta relativa, ej. "imagenes/CH.png"

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nickname, String correoElectronico, String nombre,
                      String apellido, String fecNac, String tipoUsuario) {
        this(nickname, correoElectronico, nombre, apellido, fecNac, tipoUsuario, null);
    }

    public UsuarioDTO(String nickname, String correoElectronico, String nombre,
                      String apellido, String fecNac, String tipoUsuario, String imagen) {
        this.nickname = nickname;
        this.correoElectronico = correoElectronico;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecNac = fecNac;
        this.tipoUsuario = tipoUsuario;
        this.imagen = imagen;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getFecNac() { return fecNac; }
    public void setFecNac(String fecNac) { this.fecNac = fecNac; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}