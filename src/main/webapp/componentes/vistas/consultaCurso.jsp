<%-- 
    Document   : consultaCurso.jsp
    Created on : Oct 7, 2026, 8:28:02 PM
    Author     : adolfo
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Datatypes.DTCurso" %>
<%@ page import="Datatypes.DTEdicionCurso" %>
<%@ page import="Datatypes.DTProgramaFormacion" %>

<%
    DTCurso curso = (DTCurso) request.getAttribute("cursoSeleccionado");
    List<DTEdicionCurso> ediciones = (List<DTEdicionCurso>) request.getAttribute("listaEdiciones");
    List<DTProgramaFormacion> programas = (List<DTProgramaFormacion>) request.getAttribute("listaProgramas");
%>

<div class="card shadow-sm">
    <div class="card-header bg-primary text-white">
        <h4 class="card-title mb-0">
            <i class="bi bi-book me-2"></i>Consulta de Curso
        </h4>
    </div>
    <div class="card-body">
        
        <% if (curso != null) { %>
            <div class="row mb-4">
                
                <!-- Imagen del Curso (Requerimiento Especial 7.2) -->
                <div class="col-md-4 text-center mb-3 mb-md-0">
                    <% if (curso.getImagen() != null && !curso.getImagen().trim().isEmpty()) { %>
                        <img src="<%= request.getContextPath() %>/imagenes?id=<%= curso.getImagen() %>" 
                             alt="<%= curso.getNombre() %>" class="img-fluid rounded border shadow-sm">
                    <% } else { %>
                        <img src="<%= request.getContextPath() %>/assets/img/default-course.png" 
                             alt="Sin imagen" class="img-fluid rounded border shadow-sm">
                    <% } %>
                </div>

                <!-- Detalles Principales -->
                <div class="col-md-8">
                    <h2 class="fw-bold text-primary mb-1"><%= curso.getNombre() %></h2>
                    <p class="text-muted mb-2">
                        <i class="bi bi-building me-1"></i><strong>Instituto:</strong> 
                        <%= curso.getInstituto() != null ? curso.getInstituto() : "No especificado" %>
                    </p>
                    
                    <% if (curso.getUrl() != null && !curso.getUrl().trim().isEmpty()) { %>
                        <p class="mb-3">
                            <i class="bi bi-link-45deg me-1"></i><strong>Sitio Web:</strong> 
                            <a href="<%= curso.getUrl() %>" target="_blank" class="text-decoration-underline"><%= curso.getUrl() %></a>
                        </p>
                    <% } %>

                    <div class="p-3 bg-light rounded border mb-3">
                        <p class="mb-0"><%= curso.getDescripcion() %></p>
                    </div>

                    <!-- Métricas -->
                    <div class="row g-2 text-center">
                        <div class="col-4">
                            <div class="p-2 border rounded bg-white">
                                <small class="text-muted d-block">Duración</small>
                                <strong><%= curso.getDuracion() %></strong>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="p-2 border rounded bg-white">
                                <small class="text-muted d-block">Horas</small>
                                <strong><%= curso.getCantHoras() %> hs</strong>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="p-2 border rounded bg-white">
                                <small class="text-muted d-block">Créditos</small>
                                <strong><%= curso.getCantCreditos() %></strong>
                            </div>
                        </div>
                    </div>

                    <!-- Categorías -->
                    <div class="mt-3">
                        <strong>Categorías:</strong>
                        <% if (curso.getCategorias() != null && !curso.getCategorias().isEmpty()) { 
                            for (String cat : curso.getCategorias()) { %>
                                <span class="badge bg-secondary me-1"><%= cat %></span>
                        <%  } 
                           } else { %>
                            <span class="text-muted small">Sin categorías asignadas</span>
                        <% } %>
                    </div>

                    <!-- Previas -->
                    <div class="mt-2">
                        <strong>Previas requeridas:</strong>
                        <% if (curso.getPrevias() != null && !curso.getPrevias().isEmpty()) { 
                            for (String previa : curso.getPrevias()) { %>
                                <span class="badge bg-info text-dark me-1"><%= previa %></span>
                        <%  } 
                           } else { %>
                            <span class="text-muted small">Sin previas requeridas</span>
                        <% } %>
                    </div>
                </div>
            </div>

            <hr>

            <!-- Sección Inferior: Ediciones y Programas de Formación -->
            <div class="row">
                
                <!-- Ediciones del Curso -->
                <div class="col-md-6 mb-3">
                    <h5 class="fw-bold text-secondary mb-3"><i class="bi bi-calendar-event me-2"></i>Ediciones del Curso</h5>
                    <% if (ediciones != null && !ediciones.isEmpty()) { %>
                        <div class="list-group">
                            <% for (DTEdicionCurso ed : ediciones) { %>
                                <a href="ConsultaEdicion?nombre=<%= ed.getNombre() %>" 
                                   class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="fw-bold"><%= ed.getNombre() %></div>
                                        <small class="text-muted">Inicio: <%= ed.getFechaInicio() %></small>
                                    </div>
                                    <i class="bi bi-chevron-right"></i>
                                </a>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p class="text-muted italic small">No hay ediciones registradas para este curso.</p>
                    <% } %>
                </div>

                <!-- Programas de Formación -->
                <div class="col-md-6 mb-3">
                    <h5 class="fw-bold text-secondary mb-3"><i class="bi bi-diagram-3 me-2"></i>Programas de Formación</h5>
                    <% if (programas != null && !programas.isEmpty()) { %>
                        <div class="list-group">
                            <% for (DTProgramaFormacion prog : programas) { %>
                                <a href="ConsultaPrograma?nombre=<%= prog.getNombre() %>" 
                                   class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="fw-bold"><%= prog.getNombre() %></div>
                                        <small class="text-muted"><%= prog.getDescripcion() %></small>
                                    </div>
                                    <i class="bi bi-chevron-right"></i>
                                </a>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p class="text-muted italic small">Este curso no pertenece a ningún programa de formación.</p>
                    <% } %>
                </div>

            </div>

        <% } else { %>
            <div class="alert alert-warning text-center" role="alert">
                <i class="bi bi-exclamation-triangle me-2"></i>No se ha encontrado la información del curso seleccionado.
            </div>
        <% } %>

    </div>
</div>
