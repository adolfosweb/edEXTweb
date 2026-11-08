package Controladores;

import Logica.Categoria;

public interface IControladorCategoria {

    boolean altaCategoria(Categoria categoria);

    Categoria buscarCategoria(String nombre);
}