<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Datatypes.DtCurso"%>
<%@page import="Datatypes.DtEdicionCurso"%>
<%@page import="Datatypes.DtProgramaFormacion"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%
    DtCurso curso = (DtCurso) request.getAttribute("curso");
    DtEdicionCurso edicionSeleccionada = (DtEdicionCurso) request.getAttribute("edicionSeleccionada");
    DtProgramaFormacion programaSeleccionado = (DtProgramaFormacion) request.getAttribute("programaSeleccionado");
    String urlCurso = request.getContextPath() + "/SvltConsultarCurso?nombre="
            + URLEncoder.encode(curso.getNombre(), "UTF-8");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Información del curso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .course-image-placeholder {
            min-height: 240px;
            background-color: #e9ecef;
            border: 1px solid #dee2e6;
        }
        .course-image { width: 100%; min-height: 240px; max-height: 360px; object-fit: cover; }
    </style>
</head>
<body>
    <jsp:include page="/componentes/header.jsp" />
    <div class="d-flex">
        <jsp:include page="/componentes/menulateral.jsp" />
        <main class="container-fluid bg-light p-4">
            <h1 class="mb-4"><%= escapeHtml(curso.getNombre()) %></h1>
            <div class="row g-4">
                <div class="col-12 col-lg-5">
                    <% if (curso.getUrlFoto() != null && !curso.getUrlFoto().trim().isEmpty()) { %>
                        <img class="course-image rounded" src="<%= escapeHtml(curso.getUrlFoto()) %>" alt="Imagen de <%= escapeHtml(curso.getNombre()) %>">
                    <% } else { %>
                        <div class="course-image-placeholder rounded d-flex align-items-center justify-content-center text-secondary"
                             role="img" aria-label="El curso no tiene una imagen asociada">
                            <span>Sin imagen asociada</span>
                        </div>
                    <% } %>
                </div>
                <div class="col-12 col-lg-7">
                    <dl class="row">
                        <dt class="col-sm-4">Descripción</dt>
                        <dd class="col-sm-8"><%= escapeHtml(curso.getDescripcion()) %></dd>

                        <dt class="col-sm-4">Duración</dt>
                        <dd class="col-sm-8"><%= escapeHtml(curso.getDuracion()) %></dd>

                        <dt class="col-sm-4">Cantidad de horas</dt>
                        <dd class="col-sm-8"><%= curso.getCantHoras() %></dd>

                        <dt class="col-sm-4">Créditos</dt>
                        <dd class="col-sm-8"><%= curso.getCantCreditos() %></dd>

                        <dt class="col-sm-4">Instituto</dt>
                        <dd class="col-sm-8"><%= escapeHtml(curso.getInstituto()) %></dd>

                        <dt class="col-sm-4">Fecha de registro</dt>
                        <dd class="col-sm-8">
                            <%= curso.getFecRegistro() == null ? "No especificada" : new SimpleDateFormat("dd/MM/yyyy").format(curso.getFecRegistro()) %>
                        </dd>

                        <dt class="col-sm-4">URL</dt>
                        <dd class="col-sm-8">
                            <% if (curso.getUrl() == null || curso.getUrl().trim().isEmpty()) { %>
                                No especificada
                            <% } else { %>
                                <%= escapeHtml(curso.getUrl()) %>
                            <% } %>
                        </dd>

                        <dt class="col-sm-4">Cursos previos</dt>
                        <dd class="col-sm-8">
                            <% List<String> previas = curso.getPrevias();
                               if (previas == null || previas.isEmpty()) { %>
                                No tiene cursos previos.
                            <% } else {
                                   for (int i = 0; i < previas.size(); i++) {
                                       if (i > 0) { %>, <% }
                            %><%= escapeHtml(previas.get(i)) %><%
                                   }
                               } %>
                        </dd>
                    </dl>
                </div>
            </div>
            <section class="mt-5">
                <h2>Ediciones del curso</h2>
                <% List<DtEdicionCurso> ediciones = curso.getEdiciones();
                   if (ediciones == null || ediciones.isEmpty()) { %>
                    <p>Este curso no tiene ediciones asociadas.</p>
                <% } else { %>
                    <ul class="list-group">
                        <% for (DtEdicionCurso edicion : ediciones) { %>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <span>
                                    <strong><%= escapeHtml(edicion.getNombre()) %></strong>
                                    — <%= formatDate(edicion.getFechaI()) %> a <%= formatDate(edicion.getFechaF()) %>
                                </span>
                                <a class="btn btn-outline-primary btn-sm"
                                   href="<%= urlCurso %>&amp;edicion=<%= URLEncoder.encode(edicion.getNombre(), "UTF-8") %>">
                                    Ver información
                                </a>
                            </li>
                        <% } %>
                    </ul>
                <% } %>
            </section>
            <section class="mt-5">
                <h2>Programas de formación asociados</h2>
                <% List<DtProgramaFormacion> programas = curso.getProgramas();
                   if (programas == null || programas.isEmpty()) { %>
                    <p>Este curso no está asociado a programas de formación.</p>
                <% } else { %>
                    <ul class="list-group">
                        <% for (DtProgramaFormacion programa : programas) { %>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <span><strong><%= escapeHtml(programa.getNombre()) %></strong></span>
                                <a class="btn btn-outline-primary btn-sm"
                                   href="<%= urlCurso %>&amp;programa=<%= URLEncoder.encode(programa.getNombre(), "UTF-8") %>">
                                    Ver información
                                </a>
                            </li>
                        <% } %>
                    </ul>
                <% } %>
            </section>
            <% if (edicionSeleccionada != null) { %>
                <section class="card mt-5">
                    <div class="card-header"><h2 class="h5 mb-0">Edición: <%= escapeHtml(edicionSeleccionada.getNombre()) %></h2></div>
                    <div class="card-body">
                        <dl class="row mb-0">
                            <dt class="col-sm-4">Curso</dt>
                            <dd class="col-sm-8"><%= escapeHtml(edicionSeleccionada.getNombreCurso()) %></dd>
                            <dt class="col-sm-4">Fecha de inicio</dt>
                            <dd class="col-sm-8"><%= formatDate(edicionSeleccionada.getFechaI()) %></dd>
                            <dt class="col-sm-4">Fecha de fin</dt>
                            <dd class="col-sm-8"><%= formatDate(edicionSeleccionada.getFechaF()) %></dd>
                            <dt class="col-sm-4">Cupo</dt>
                            <dd class="col-sm-8"><%= edicionSeleccionada.getCupo() > 0 ? edicionSeleccionada.getCupo() : "Sin límite especificado" %></dd>
                            <dt class="col-sm-4">Fecha de publicación</dt>
                            <dd class="col-sm-8"><%= formatDate(edicionSeleccionada.getFechaPub()) %></dd>
                            <dt class="col-sm-4">Docentes participantes</dt>
                            <dd class="col-sm-8"><%= formatNames(edicionSeleccionada.getNombresDocentes()) %></dd>
                            <dt class="col-sm-4">Estudiantes inscriptos</dt>
                            <dd class="col-sm-8"><%= formatNames(edicionSeleccionada.getNombresInscriptos()) %></dd>
                        </dl>
                    </div>
                </section>
            <% } %>
            <% if (programaSeleccionado != null) { %>
                <section class="card mt-5">
                    <div class="card-header"><h2 class="h5 mb-0">Programa: <%= escapeHtml(programaSeleccionado.getNombre()) %></h2></div>
                    <div class="card-body">
                        <dl class="row mb-0">
                            <dt class="col-sm-4">Descripción</dt>
                            <dd class="col-sm-8"><%= escapeHtml(programaSeleccionado.getDescripcion()) %></dd>
                            <dt class="col-sm-4">Fecha de inicio</dt>
                            <dd class="col-sm-8"><%= formatDate(programaSeleccionado.getFechaInicio()) %></dd>
                            <dt class="col-sm-4">Fecha de fin</dt>
                            <dd class="col-sm-8"><%= formatDate(programaSeleccionado.getFechaFin()) %></dd>
                            <dt class="col-sm-4">Fecha de alta</dt>
                            <dd class="col-sm-8"><%= formatDate(programaSeleccionado.getFechaAlta()) %></dd>
                            <dt class="col-sm-4">Cursos asociados</dt>
                            <dd class="col-sm-8"><%= formatNames(programaSeleccionado.getNombresCursos()) %></dd>
                            <dt class="col-sm-4">Estudiantes inscriptos</dt>
                            <dd class="col-sm-8"><%= formatNames(programaSeleccionado.getNombresInscriptos()) %></dd>
                        </dl>
                    </div>
                </section>
            <% } %>
            <a href="<%= request.getContextPath() %>/SvltListarCursos" class="btn btn-secondary mt-4">Volver a cursos</a>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<%!
    private String formatDate(LocalDate date) {
        return date == null ? "No especificada" : date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return "Ninguno";
        }
        StringBuilder result = new StringBuilder();
        for (String name : names) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(escapeHtml(name));
        }
        return result.toString();
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
