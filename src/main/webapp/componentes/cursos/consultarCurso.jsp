<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Datatypes.DtCurso"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%
    DtCurso curso = (DtCurso) request.getAttribute("curso");
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
        .avatar-sm { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
        .course-image-placeholder {
            min-height: 240px;
            background-color: #e9ecef;
            border: 1px solid #dee2e6;
        }
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
                    <div class="course-image-placeholder rounded" aria-label="Espacio reservado para la imagen del curso"></div>
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
            <a href="<%= request.getContextPath() %>/SvltListarCursos" class="btn btn-secondary mt-4">Volver a cursos</a>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<%!
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
