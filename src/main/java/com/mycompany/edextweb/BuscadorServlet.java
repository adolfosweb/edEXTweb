package com.mycompany.edextweb;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import Controladores.Buscador;
import DTO.ResultadoBusqueda;

@WebServlet(name = "BuscadorServlet", urlPatterns = {"/buscador"})
public class BuscadorServlet extends HttpServlet {
    
    private Buscador servicioBusqueda = new Buscador();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println(">>> ENTRO AL BuscadorServlet");
        
        String query = request.getParameter("q");
        String filtro = request.getParameter("filtro");
        String orden = request.getParameter("orden");

        query = query == null ? "" : query.trim();
        filtro = filtro == null ? "todos" : filtro;
        orden = orden == null ? "nombre_asc" : orden;

        List<ResultadoBusqueda> resultados = servicioBusqueda.buscar(query, filtro, orden);

        request.setAttribute("resultados", resultados);
        request.setAttribute("busquedaActual", query);
        request.setAttribute("filtroActual", filtro);
        request.setAttribute("ordenActual", orden);

        request.getRequestDispatcher("/componentes/resultadoBusqueda.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
