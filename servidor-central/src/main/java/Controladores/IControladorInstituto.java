/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Controladores;
import Logica.Instituto;
import java.util.Map;

/**
 *
 * @author manug
 */
public interface IControladorInstituto {

    boolean altaInstituto(Instituto instituto);
    
    Instituto buscarInstituto(String nombre);
    
    Map<String, Instituto> listarInstitutos();
}
