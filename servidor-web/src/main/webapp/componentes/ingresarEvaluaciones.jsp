<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ingresar evaluaciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <%@ include file="menulateral.jsp" %>
        <main class="container-fluid py-4 px-4">
            <h2 class="mb-2">
                <i class="bi bi-clipboard-check me-2"></i>
                Ingresar evaluaciones
            </h2>
            <p class="text-muted mb-4">
                Consultá los estudiantes aceptados e ingresá sus calificaciones.
            </p>
            <c:if test="${not empty mensajeEvaluaciones}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    <c:out value="${mensajeEvaluaciones}" />
                    <button type="button" class="btn-close"
                            data-bs-dismiss="alert" aria-label="Cerrar"></button>
                </div>
            </c:if>
            <c:if test="${not empty mensajeError}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${mensajeError}" />
                </div>
            </c:if>
            <!-- Seleccionar instituto -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h5 class="card-title">Seleccionar instituto</h5>
                    <div class="list-group">
                        <c:forEach var="instituto" items="${institutos}">
                            <c:url var="urlInstituto" value="/ingresarEvaluaciones">
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
                        <h5 class="card-title">Seleccionar curso</h5>
                        <c:choose>
                            <c:when test="${not empty cursos}">
                                <div class="list-group">
                                    <c:forEach var="curso" items="${cursos}">
                                        <c:url var="urlCurso" value="/ingresarEvaluaciones">
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
            <!-- Seleccionar edición -->
            <c:if test="${not empty cursoSeleccionado}">
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <h5 class="card-title">Seleccionar edición</h5>
                        <c:choose>
                            <c:when test="${not empty ediciones}">
                                <div class="list-group">
                                    <c:forEach var="edicion" items="${ediciones}">
                                        <c:url var="urlEdicion" value="/ingresarEvaluaciones">
                                            <c:param name="instituto" value="${institutoSeleccionado}" />
                                            <c:param name="curso" value="${cursoSeleccionado}" />
                                            <c:param name="edicion" value="${edicion.nombre}" />
                                        </c:url>
                                        <a href="${urlEdicion}"
                                           class="list-group-item list-group-item-action
                                           ${edicionSeleccionada eq edicion.nombre ? 'active' : ''}">
                                            <strong><c:out value="${edicion.nombre}" /></strong>
                                            <span class="ms-2">
                                                Inicio: <c:out value="${edicion.fechaInicio}" />
                                            </span>
                                            <span class="ms-2">
                                                Cupo: <c:out value="${edicion.cupo}" />
                                            </span>
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-0">
                                    No hay ediciones disponibles para este curso.
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            <!-- Ingresar notas -->
            <c:if test="${not empty edicionSeleccionada}">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
                            <div>
                                <h5 class="card-title mb-1">
                                    Evaluaciones de la edición
                                </h5>
                                <p class="text-muted mb-0">
                                    Edición: <strong><c:out value="${edicionSeleccionada}" /></strong>
                                </p>
                            </div>
                            <c:choose>
                                <c:when test="${edicionSeleccionadaDTO.cerrada}">
                                    <span class="badge text-bg-secondary fs-6">
                                        <i class="bi bi-lock-fill me-1"></i>
                                        Edición cerrada
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge text-bg-success fs-6">
                                        Edición abierta
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <c:choose>
                            <c:when test="${not empty inscripciones}">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/ingresarEvaluaciones">
                                    <input type="hidden" name="instituto" value="<c:out value='${institutoSeleccionado}' />">
                                    <input type="hidden" name="curso" value="<c:out value='${cursoSeleccionado}' />">
                                    <input type="hidden" name="edicion" value="<c:out value='${edicionSeleccionada}' />">

                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Estudiante</th>
                                                    <th>Fecha de inscripción</th>
                                                    <th style="width: 180px;">Nota (1-12)</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="inscripcion" items="${inscripciones}">
                                                    <tr>
                                                        <td>
                                                            <c:if test="${not empty inscripcion.estudiante}">
                                                                <c:out value="${inscripcion.estudiante.nombre}" />
                                                                <c:out value="${inscripcion.estudiante.apellido}" />
                                                                <small class="text-muted d-block">
                                                                    <c:out value="${inscripcion.estudiante.nickname}" />
                                                                </small>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:out value="${inscripcion.fechaInscripcion}" />
                                                        </td>
                                                        <td>
                                                            <input type="number"
                                                                   class="form-control"
                                                                   name="nota_${inscripcion.id}"
                                                                   min="1"
                                                                   max="12"
                                                                   step="1"
                                                                   value="${inscripcion.nota}"
                                                                   placeholder="Sin nota"
                                                                   ${edicionSeleccionadaDTO.cerrada ? 'disabled' : ''}>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:if test="${not edicionSeleccionadaDTO.cerrada}">
                                        <button type="submit"
                                                name="accion"
                                                value="guardar"
                                                class="btn btn-primary">
                                            <i class="bi bi-save me-1"></i>
                                            Guardar evaluaciones
                                        </button>
                                    </c:if>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info mb-0">
                                    No hay estudiantes aceptados en esta edición.
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <hr>
                        <c:if test="${not edicionSeleccionadaDTO.cerrada}">
                            <form method="post"
                                  action="${pageContext.request.contextPath}/ingresarEvaluaciones"
                                  onsubmit="return confirm('¿Estás seguro de cerrar esta edición? No podrás modificar las evaluaciones después.');">

                                <input type="hidden" name="instituto" value="<c:out value='${institutoSeleccionado}' />">
                                <input type="hidden" name="curso" value="<c:out value='${cursoSeleccionado}' />">
                                <input type="hidden" name="edicion" value="<c:out value='${edicionSeleccionada}' />">
                                <button type="submit"
                                        name="accion"
                                        value="cerrar"
                                        class="btn btn-outline-danger">
                                    <i class="bi bi-lock me-1"></i>
                                    Cerrar edición
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
