<%-- 
    Document   : listarCursos
    Created on : Oct 7, 2026, 9:49:45 PM
    Author     : adolfo
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Datatypes.DTCurso" %>

<%
    List<DTCurso> cursos = (List<DTCurso>) request.getAttribute("listaCursos");
%>

<div class="card shadow-sm">
    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
        <h4 class="card-title mb-0">
            <i class="bi bi-list-task me-2"></i>Listado de Cursos
        </h4>
    </div>
    <div class="card-body">

        <% if (cursos != null && !cursos.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>Nombre</th>
                            <th>Instituto</th>
                            <th>Duración</th>
                            <th>Horas</th>
                            <th>Créditos</th>
                            <th class="text-center">Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (DTCurso c : cursos) { %>
                            <tr>
                                <td class="fw-bold text-primary"><%= c.getNombre() %></td>
                                <td><%= c.getInstituto() %></td>
                                <td><%= c.getDuracion() %></td>
                                <td><%= c.getCantHoras() %> hs</td>
                                <td><%= c.getCantCreditos() %></td>
                                <td class="text-center">
                                    <a href="<%= request.getContextPath() %>/ConsultaCurso?nombre=<%= c.getNombre() %>" 
                                       class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-eye me-1"></i>Ver Detalle
                                    </a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } else { %>
            <div class="alert alert-info text-center" role="alert">
                <i class="bi bi-info-circle me-2"></i>No hay cursos registrados en el sistema.
            </div>
        <% } %>

    </div>
</div>
