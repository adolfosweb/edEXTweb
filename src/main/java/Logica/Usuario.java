/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String nickname;

    private String correoElectronico;
    private String nombre;
    private String apellido;
    private LocalDate fecNac;
    private String password;

    public Usuario() {
    }

    public Usuario(String nickname, String correoElectronico,
            String nombre, String apellido, LocalDate fecNac) {

        this.nickname = nickname;
        this.correoElectronico = correoElectronico;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecNac = fecNac;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFecNac() {
        return fecNac;
    }

    public void setFecNac(LocalDate fecNac) {
        this.fecNac = fecNac;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public int hashCode() {
        return nickname != null ? nickname.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Usuario)) {
            return false;
        }

        Usuario other = (Usuario) obj;

        if (nickname == null && other.nickname != null) {
            return false;
        }

        if (nickname != null && !nickname.equals(other.nickname)) {
            return false;
        }

        return true;
    }
}