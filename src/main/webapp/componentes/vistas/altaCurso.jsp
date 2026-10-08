<%-- 
    Document   : altaCurso
    Created on : Oct 6, 2026, 10:35:18 PM
    Author     : adolfo
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<%--
    Formulario de Alta de Curso para ser incluido en la vista principal
    Cumple con el requerimiento de registro de cursos del proyecto edEXT
--%>

<div class="card shadow-sm">
    <div class="card-header bg-primary text-white">
        <h4 class="card-title mb-0">
            <i class="bi bi-journal-plus me-2"></i>Alta de Curso
        </h4>
    </div>
    <div class="card-body">
        
        <%-- Mensaje de error / aviso opcional si viene de una redirección --%>
        <% if (request.getAttribute("mensajeError") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= request.getAttribute("mensajeError") %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <form action="AltaCurso" method="POST" enctype="multipart/form-data">
            
            <!-- Selección de Instituto -->
            <div class="mb-3">
                <label for="instituto" class="form-label fw-bold">Instituto <span class="text-danger">*</span></label>
                <select class="form-select" id="instituto" name="instituto" required>
                    <option value="" selected disabled>Seleccione un instituto...</option>
                    <% 
                        List<String> institutos = (List<String>) request.getAttribute("listaInstitutos");
                        if (institutos != null) {
                            for (String inst : institutos) {
                    %>
                                <option value="<%= inst %>"><%= inst %></option>
                    <% 
                            }
                        } else {
                    %>
                        <%-- Valores por defecto en caso de prueba sin servlet previo --%>
                        <option value="INCO">INCO - Instituto de Computación</option>
                        <option value="IMERL">IMERL</option>
                        <option value="Fisica">Instituto de Física</option>
                    <% } %>
                </select>
            </div>

            <!-- Nombre del Curso (Único) -->
            <div class="mb-3">
                <label for="nombre" class="form-label fw-bold">Nombre del Curso <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ej. Taller de Robótica Educativa" required>
            </div>

            <!-- Descripción -->
            <div class="mb-3">
                <label for="descripcion" class="form-label fw-bold">Descripción <span class="text-danger">*</span></label>
                <textarea class="form-control" id="descripcion" name="descripcion" rows="4" placeholder="Ingrese una breve descripción del curso..." required></textarea>
            </div>

            <div class="row">
                <!-- Duración -->
                <div class="col-md-4 mb-3">
                    <label for="duracion" class="form-label fw-bold">Duración <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="duracion" name="duracion" placeholder="Ej. 2 meses, 8 semanas" required>
                </div>

                <!-- Cantidad de Horas -->
                <div class="col-md-4 mb-3">
                    <label for="cantHoras" class="form-label fw-bold">Cantidad de Horas <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="cantHoras" name="cantHoras" min="1" placeholder="Ej. 60" required>
                </div>

                <!-- Cantidad de Créditos -->
                <div class="col-md-4 mb-3">
                    <label for="creditos" class="form-label fw-bold">Créditos <span class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="creditos" name="creditos" min="0" placeholder="Ej. 10" required>
                </div>
            </div>

            <!-- URL asociada -->
            <div class="mb-3">
                <label for="url" class="form-label fw-bold">URL Asociada <span class="text-danger">*</span></label>
                <input type="url" class="form-control" id="url" name="url" placeholder="https://eva.fing.edu.uy/course/view.php?id=..." required>
            </div>

            <!-- Selección de Categorías (Múltiple) -->
            <div class="mb-3">
                <label for="categorias" class="form-label fw-bold">Categorías <span class="text-danger">*</span></label>
                <select class="form-select" id="categorias" name="categorias" multiple aria-label="Selección múltiple de categorías" required>
                    <% 
                        List<String> categorias = (List<String>) request.getAttribute("listaCategorias");
                        if (categorias != null) {
                            for (String cat : categorias) {
                    %>
                                <option value="<%= cat %>"><%= cat %></option>
                    <% 
                            }
                        } else {
                    %>
                        <option value="Social">Social</option>
                        <option value="Industrial">Industrial</option>
                        <option value="Educativos">Educativos</option>
                        <option value="Interdisciplinario">Interdisciplinario</option>
                    <% } %>
                </select>
                <div class="form-text">Mantén presionada la tecla Ctrl (o Cmd en Mac) para seleccionar más de una categoría[cite: 1].</div>
            </div>

            <!-- Previaturas / Cursos Previos (Opcional) -->
            <div class="mb-3">
                <label for="previas" class="form-label fw-bold">Previas (Opcional)</label>
                <select class="form-select" id="previas" name="previas" multiple aria-label="Selección de previas">
                    <% 
                        List<String> cursosPrevios = (List<String>) request.getAttribute("listaCursosPrevios");
                        if (cursosPrevios != null) {
                            for (String curso : cursosPrevios) {
                    %>
                                <option value="<%= curso %>"><%= curso %></option>
                    <% 
                            }
                        } else {
                    %>
                        <option value="Programacion1">Programación 1</option>
                        <option value="Programacion2">Programación 2</option>
                        <option value="Lógica">Lógica</option>
                    <% } %>
                </select>
                <div class="form-text">Selecciona uno o más cursos necesarios como previa[cite: 1].</div>
            </div>

            <!-- Imagen del Curso (Opcional) -->
            <div class="mb-4">
                <label for="imagen" class="form-label fw-bold">Imagen del Curso (Opcional)</label>
                <input class="form-control" type="file" id="imagen" name="imagen" accept="image/*">
            </div>

            <!-- Botones de Acción -->
            <div class="d-flex justify-content-end gap-2">
                <a href="Home" class="btn btn-secondary">Cancelar</a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-circle me-1"></i> Dar de Alta
                </button>
            </div>

        </form>
    </div>
</div>
