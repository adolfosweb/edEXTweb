<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Logica.InscripcionCurso"%>
<%@page import="Logica.EdicionCurso"%>
<%@page import="Logica.Curso"%>
<%@page import="Logica.EstadoInscripcion"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resultados de inscripciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp"/>
    <div class="d-flex">
        <jsp:include page="menulateral.jsp"/>
        <main class="container-fluid py-4 px-4">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h2 class="mb-2">
                        <i class="bi bi-card-checklist me-2"></i>
                        Resultados de inscripciones
                    </h2>
                    <p class="text-muted mb-4">Consulta el estado de tus inscripciones a ediciones de cursos. </p>
                    <%
                        List<InscripcionCurso> inscripciones =
                            (List<InscripcionCurso>) request.getAttribute("inscripciones");
                    %>
                    <% if (inscripciones == null || inscripciones.isEmpty()) { %>
                        <div class="alert alert-info mb-0">
                            Todavía no tienes inscripciones a ediciones de cursos.
                        </div>
                    <% } else { %>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Curso</th>
                                        <th>Edición</th>
                                        <th>Fecha de inscripción</th>
                                        <th>Estado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (InscripcionCurso inscripcion : inscripciones) {
                                        EdicionCurso edicion = inscripcion.getCurso();
                                        Curso curso = edicion != null ? edicion.getCurso() : null;

                                        EstadoInscripcion estado = inscripcion.getEstado();

                                        String claseEstado = "secondary";
                                        String textoEstado = "Sin estado";

                                        if (estado != null) {
                                            textoEstado = estado.toString();
                                            switch (estado) {
                                                case INSCRIPTO:
                                                    claseEstado = "primary";
                                                    break;
                                                case ACEPTADA:
                                                    claseEstado = "success";
                                                    break;
                                                case RECHAZADA:
                                                    claseEstado = "danger";
                                                    break;
                                            }
                                        }
                                    %>
                                        <tr>
                                            <td>
                                                <%= curso != null ? curso.getNombre() : "Curso no disponible" %>
                                            </td>
                                            <td>
                                                <%= edicion != null ? edicion.getNombre() : "Edición no disponible" %>
                                            </td>
                                            <td>
                                                <%= inscripcion.getFechaIns() != null ? inscripcion.getFechaIns() : "No disponible" %>
                                            </td>
                                            <td>
                                                <span class="badge text-bg-<%= claseEstado %>"><%= textoEstado %></span>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } %>
                    <button type="button" class="btn btn-outline-secondary" onclick="history.back()">
                        <i class="bi bi-arrow-left me-1"></i>
                        Volver
                    </button>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
