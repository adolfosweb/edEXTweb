/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;
import Logica.ProgramaFormacion;
import java.util.Map;

/**
 *
 * @author manug
 */
public interface IControladorPrograma {
    
    boolean altaPrograma(ProgramaFormacion programa);

    ProgramaFormacion buscarPrograma(String nombre);

    boolean modificarPrograma(ProgramaFormacion programa);

    boolean eliminarPrograma(String nombre);

    Map<String, ProgramaFormacion> listarProgramas();
    
    boolean agregarCurso(String nombrePrograma, String nombreCurso);
}
    

