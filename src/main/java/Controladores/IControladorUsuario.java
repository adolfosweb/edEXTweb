/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;
import Logica.Usuario;
import java.util.Map;

/**
 *
 * @author manug
 */
public interface IControladorUsuario {
    
    boolean altaUsuario(Usuario usuario, String nombreInstituto);

    Usuario buscarUsuario(String nickname);

    void modificarUsuario(Usuario usuario);

    void eliminarUsuario(String nickname);

    Map<String, Usuario> listarUsuarios();
}
