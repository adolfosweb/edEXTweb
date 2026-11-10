<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ver resultados de evaluaciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <%@ include file="menulateral.jsp" %>
        <main class="container-fluid py-4 px-4">
            <h2 class="mb-2">
                <i class="bi bi-journal-check me-2"></i>
                Ver resultados de evaluaciones
            </h2>
            <p class="text-muted mb-4">
                Consultá las actas de resultados de las ediciones de cursos cerradas.
            </p>
            <c:if test="${not empty mensajeError}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${mensajeError}" />
                </div>
            </c:if>
            <!-- Seleccionar instituto -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h5 class="card-title">
                        Seleccionar instituto
                    </h5>
                    <div class="list-group">
                        <c:forEach var="instituto" items="${institutos}">
                            <c:url var="urlInstituto" value="/verResultadosEvaluaciones">
                                <c:param name="instituto" value="${instituto.nombre}" />
                            </c:url>
                            <a href="${urlInstituto}"
                               class="list-group-item list-group-item-action
                               ${institutoSeleccionado eq instituto.nombre ? 'active' : ''}">
                                <c:out value="${instituto.nombre}" />
                            </a>
                        </c:forEach>
                    </div>
                    <c:if test="${empty institutos}">
                        <p class="text-muted mb-0">
                            No tienes institutos asociados.
                        </p>
                    </c:if>
                </div>
            </div>
            <!-- Seleccionar curso -->
            <c:if test="${not empty institutoSeleccionado}">
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <h5 class="card-title">
                            Seleccionar curso
                        </h5>
                        <c:choose>
                            <c:when test="${not empty cursos}">
                                <div class="list-group">
                                    <c:forEach var="curso" items="${cursos}">
                                        <c:url var="urlCurso" value="/verResultadosEvaluaciones">
                                            <c:param name="instituto" value="${institutoSeleccionado}" />
                                            <c:param name="curso" value="${curso.nombre}" />
                                        </c:url>
                                        <a href="${urlCurso}"
                                           class="list-group-item list-group-item-action
                                           ${cursoSeleccionado eq curso.nombre ? 'active' : ''}">
                                            <c:out value="${curso.nombre}" />
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-0">
                                    No hay cursos disponibles para este instituto.
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            <!-- Seleccionar edición cerrada -->
            <c:if test="${not empty cursoSeleccionado}">
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <h5 class="card-title">
                            Seleccionar edición cerrada
                        </h5>
                        <c:choose>
                            <c:when test="${not empty ediciones}">
                                <div class="list-group">
                                    <c:forEach var="edicion" items="${ediciones}">
                                        <c:url var="urlEdicion" value="/verResultadosEvaluaciones">
                                            <c:param name="instituto" value="${institutoSeleccionado}" />
                                            <c:param name="curso" value="${cursoSeleccionado}" />
                                            <c:param name="edicion" value="${edicion.nombre}" />
                                        </c:url>
                                        <a href="${urlEdicion}"
                                           class="list-group-item list-group-item-action
                                           ${edicionSeleccionada eq edicion.nombre ? 'active' : ''}">
                                            <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                                                <strong>
                                                    <c:out value="${edicion.nombre}" />
                                                </strong>
                                                <span class="badge text-bg-secondary">
                                                    <i class="bi bi-lock-fill me-1"></i>
                                                    Cerrada
                                                </span>
                                            </div>
                                            <small class="text-muted d-block mt-1">
                                                Inicio:
                                                <c:out value="${edicion.fechaInicio}" />
                                                <span class="ms-2">
                                                    Fin:
                                                    <c:out value="${edicion.fechaFin}" />
                                                </span>
                                                <span class="ms-2">
                                                    Cupo:
                                                    <c:out value="${edicion.cupo}" />
                                                </span>
                                            </small>
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-0">
                                    No hay ediciones cerradas para este curso.
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            <!-- Acta de resultados -->
            <c:if test="${not empty edicionSeleccionada}">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-4">
                            <div>
                                <h5 class="card-title mb-1">
                                    <i class="bi bi-file-earmark-text me-2"></i>
                                    Acta de resultados
                                </h5>
                                <p class="text-muted mb-0">
                                    Edición:
                                    <strong>
                                        <c:out value="${edicionSeleccionada}" />
                                    </strong>
                                </p>
                            </div>
                            <span class="badge text-bg-secondary fs-6">
                                <i class="bi bi-lock-fill me-1"></i>
                                Edición cerrada
                            </span>
                        </div>
                        <!-- Datos de la edición -->
                        <div class="row g-3 mb-4">
                            <div class="col-md-4">
                                <div class="border rounded p-3 h-100">
                                    <small class="text-muted d-block">
                                        Curso
                                    </small>
                                    <strong>
                                        <c:out value="${edicionSeleccionadaDTO.nombreCurso}" />
                                    </strong>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="border rounded p-3 h-100">
                                    <small class="text-muted d-block">
                                        Fecha de inicio
                                    </small>
                                    <strong>
                                        <c:out value="${edicionSeleccionadaDTO.fechaInicio}" />
                                    </strong>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="border rounded p-3 h-100">
                                    <small class="text-muted d-block">
                                        Fecha de finalización
                                    </small>
                                    <strong>
                                        <c:out value="${edicionSeleccionadaDTO.fechaFin}" />
                                    </strong>
                                </div>
                            </div>

                        </div>
                        <!-- Resultados -->
                        <h6 class="mb-3">
                            Estudiantes participantes
                        </h6>
                        <c:choose>
                            <c:when test="${not empty inscripciones}">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Estudiante</th>
                                                <th>Nickname</th>
                                                <th>Fecha de inscripción</th>
                                                <th class="text-center">Nota</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="inscripcion" items="${inscripciones}">
                                                <tr>
                                                    <td>
                                                        <c:if test="${not empty inscripcion.estudiante}">
                                                            <c:out value="${inscripcion.estudiante.nombre}" />
                                                            <c:out value="${inscripcion.estudiante.apellido}" />
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${not empty inscripcion.estudiante}">
                                                            <small class="text-muted">
                                                                <c:out value="${inscripcion.estudiante.nickname}" />
                                                            </small>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:out value="${inscripcion.fechaInscripcion}" />
                                                    </td>
                                                    <td class="text-center">
                                                        <c:choose>
                                                            <c:when test="${inscripcion.nota != null}">
                                                                <span class="badge text-bg-primary fs-6">
                                                                    <c:out value="${inscripcion.nota}" />
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted">
                                                                    Sin nota
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="text-muted small">
                                    Total de participantes:
                                    <strong>${inscripciones.size()}</strong>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info mb-0">
                                    No hay estudiantes aceptados en esta edición.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
