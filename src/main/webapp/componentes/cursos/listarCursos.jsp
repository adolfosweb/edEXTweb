<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Datatypes.DtCurso"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%
    List<?> cursos = (List<?>) request.getAttribute("cursos");
    Set<?> cursosEditables = (Set<?>) request.getAttribute("cursosEditables");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Listado de cursos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .avatar-sm { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
        .course-image-placeholder {
            height: 190px;
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
            <h1 class="mb-4">Listado de cursos</h1>
            <div class="row g-4">
                <% if (cursos != null && !cursos.isEmpty()) {
                    for (Object item : cursos) {
                        if (item instanceof DtCurso) {
                            DtCurso curso = (DtCurso) item;
                %>
                <div class="col-12 col-md-6 col-xl-4">
                    <article class="card h-100 shadow-sm">
                        <div class="course-image-placeholder" aria-label="Espacio reservado para la imagen del curso"></div>
                        <div class="card-body d-flex flex-column">
                            <h2 class="h5 card-title"><%= escapeHtml(curso.getNombre()) %></h2>
                            <p class="card-text"><%= escapeHtml(curso.getDescripcion()) %></p>
                            <a class="btn btn-primary mt-auto align-self-start"
                               href="<%= request.getContextPath() %>/SvltConsultarCurso?nombre=<%= java.net.URLEncoder.encode(curso.getNombre(), "UTF-8") %>">
                                Ver toda la información
                            </a>
                            <% if (cursosEditables != null && cursosEditables.contains(curso.getNombre())) { %>
                            <a class="btn btn-outline-primary mt-2 align-self-start"
                               href="<%= request.getContextPath() %>/SvltAltaEdicionCurso?curso=<%= java.net.URLEncoder.encode(curso.getNombre(), "UTF-8") %>">
                                Alta de Edición
                            </a>
                            <% } %>
                        </div>
                    </article>
                </div>
                <%      }
                    }
                } else { %>
                <div class="col">
                    <p class="text-center">No hay cursos disponibles.</p>
                </div>
                <% } %>
            </div>
            <a href="<%= request.getContextPath() %>/componentes/principal.jsp" class="btn btn-secondary mt-4">Volver</a>
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
