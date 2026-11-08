/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fabric;

import Controladores.ControladorCategoria;
import Controladores.ControladorCurso;
import Controladores.ControladorPrograma;
import Controladores.ControladorUsuario;
import Controladores.IControladorCurso;
import Controladores.IControladorPrograma;
import Controladores.IControladorUsuario;
import Controladores.ControladorInstituto;
import Controladores.IControladorCategoria;
import Controladores.IControladorInstituto;

/**
 *
 * @author manug
 */

public class Fabric {

    private static Fabric instancia;

    private IControladorUsuario controladorUsuario;
    private IControladorCurso controladorCurso;
    private IControladorPrograma controladorPrograma;
    private IControladorInstituto controladorInstituto;
    private IControladorCategoria controladorCategoria;

    private Fabric() {
        controladorUsuario = new ControladorUsuario();
        controladorCurso = new ControladorCurso();
        controladorPrograma = new ControladorPrograma();
        controladorInstituto = new ControladorInstituto();
        controladorCategoria = new ControladorCategoria();
    }

    public static Fabric getInstancia() {
        if (instancia == null) {
            instancia = new Fabric();
        }

        return instancia;
    }

    public IControladorUsuario getControladorUsuario() {
        return controladorUsuario;
    }

    public IControladorCurso getControladorCurso() {
        return controladorCurso;
    }

    public IControladorPrograma getControladorPrograma() {
        return controladorPrograma;
    }
    
    public IControladorInstituto getControladorInstituto() {
        return controladorInstituto;
}
    
    public IControladorCategoria getIControladorCategoria() {
    return controladorCategoria;
}
}