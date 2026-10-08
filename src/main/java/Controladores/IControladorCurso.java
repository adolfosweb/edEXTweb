/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;
import Logica.Curso;
import Logica.EdicionCurso;
import Logica.InscripcionCurso;
import java.util.Map;
import Datatypes.DTCurso;
import Datatypes.DTEdicionCurso;
import Datatypes.DTProgramaFormacion;
import java.util.List;

/**
 *
 * @author manug
 */
public interface IControladorCurso {
    
    boolean altaCurso(Curso curso);

    Curso buscarCurso(String nombre);

    boolean modificarCurso(Curso curso);

    boolean eliminarCurso(String nombre);

    boolean altaEdicion(EdicionCurso edicion);

    EdicionCurso buscarEdicion(String nombre);

    Map<String, Curso> listarCursos();

    Map<String, EdicionCurso> listarEdiciones();
    
    InscripcionCurso buscarInscripcion(String nickEstudiante, String nombreEdicion);

    boolean inscribirEstudiante(InscripcionCurso inscripcion);
    
    //Agregado para trabajar con servlets
    
    List<DTCurso> obtenerListaCursosDT();
    
    DTCurso obtenerInformacionCurso(String nombre);
    
    List<DTEdicionCurso> obtenerEdicionesCurso(String nombreCurso);
    
    List<DTProgramaFormacion> obtenerProgramasCurso(String nombreCurso);
    
    
}
