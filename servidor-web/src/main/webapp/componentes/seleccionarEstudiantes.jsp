<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Seleccionar estudiantes</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"  rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <%@ include file="menulateral.jsp" %>
        <main class="container-fluid py-4 px-4">
            <h2 class="mb-2">
                <i class="bi bi-person-check me-2"></i>
                Seleccionar estudiantes
            </h2>
            <p class="text-muted mb-4">Consulta las inscripciones y gestiona la selección de estudiantes.</p>
            <c:if test="${not empty mensajeSeleccion}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle me-2"></i>
                    <c:out value="${mensajeSeleccion}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
                </div>
            </c:if>
            <!-- Paso 1: seleccionar instituto -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h5 class="card-title">Seleccionar instituto</h5>
                    <div class="list-group">
                        <c:forEach var="instituto" items="${institutos}">
                            <c:url var="urlInstituto" value="/seleccionarEstudiantes">
                                <c:param name="instituto" value="${instituto.nombre}" />
                            </c:url>
                            <a href="${urlInstituto}" class="list-group-item list-group-item-action
                               ${institutoSeleccionado eq instituto.nombre ? 'active' : ''}">
                                ${instituto.nombre}
                            </a>
                        </c:forEach>
                    </div>
                    <c:if test="${empty institutos}">
                        <p class="text-muted mb-0"> No tienes institutos asociados.</p>
                    </c:if>
                </div>
            </div>
            <!-- Paso 2: seleccionar curso -->
            <c:if test="${not empty institutoSeleccionado}">
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <h5 class="card-title">Seleccionar curso</h5>
                        <c:choose>
                            <c:when test="${not empty cursos}">
                                <div class="list-group">
                                    <c:forEach var="curso" items="${cursos}">
                                        <c:url var="urlCurso" value="/seleccionarEstudiantes">
                                            <c:param name="instituto" value="${institutoSeleccionado}" />
                                            <c:param name="curso" value="${curso.nombre}" />
                                        </c:url>
                                        <a href="${urlCurso}" class="list-group-item list-group-item-action
                                           ${cursoSeleccionado eq curso.nombre ? 'active' : ''}">
                                            ${curso.nombre}
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-0">No hay cursos disponibles para este instituto.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            <!-- Paso 3: seleccionar edición -->
            <c:if test="${not empty cursoSeleccionado}">
                <div class="card shadow-sm mb-4">
                    <div class="card-body">
                        <h5 class="card-title">Seleccionar edición</h5>
                        <c:choose>
                            <c:when test="${not empty ediciones}">
                                <div class="list-group">
                                    <c:forEach var="edicion" items="${ediciones}">
                                        <c:url var="urlEdicion" value="/seleccionarEstudiantes">
                                            <c:param name="instituto" value="${institutoSeleccionado}" />
                                            <c:param name="curso" value="${cursoSeleccionado}" />
                                            <c:param name="edicion" value="${edicion.nombre}" />
                                        </c:url>
                                        <a href="${urlEdicion}" class="list-group-item list-group-item-action
                                           ${edicionSeleccionada eq edicion.nombre ? 'active' : ''}">
                                            <strong>${edicion.nombre}</strong>
                                            <span class="ms-2">Inicio: ${edicion.fechaInicio}</span>
                                            <span class="ms-2">Cupo: ${edicion.cupo}</span>
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-0">No hay ediciones disponibles para este curso.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            <!-- Paso 4: consultar inscripciones -->
            <c:if test="${not empty edicionSeleccionada}">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">Estudiantes inscriptos</h5>
                        <c:choose>
                            <c:when test="${not empty inscripciones}">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Estudiante</th>
                                                <th>Fecha de inscripción</th>
                                                <th>Estado actual</th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="inscripcion" items="${inscripciones}">
                                                <tr>
                                                    <td>
                                                        ${inscripcion.estudiante.nombre}
                                                        ${inscripcion.estudiante.apellido}
                                                        <small class="text-muted d-block">
                                                            ${inscripcion.estudiante.nickname}
                                                        </small>
                                                    </td>
                                                    <td>${inscripcion.fechaIns}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${inscripcion.estado eq 'ACEPTADA'}">
                                                                <span class="badge text-bg-success">Aceptada</span>
                                                            </c:when>
                                                            <c:when test="${inscripcion.estado eq 'RECHAZADA'}">
                                                                <span class="badge text-bg-danger">Rechazada</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge text-bg-primary">Inscripto</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <form method="post" action="${pageContext.request.contextPath}/seleccionarEstudiantes"
                                                              class="d-flex flex-wrap gap-2">
                                                            <input type="hidden" name="instituto" value="<c:out value='${institutoSeleccionado}'/>">
                                                            <input type="hidden" name="curso" value="<c:out value='${cursoSeleccionado}'/>">
                                                            <input type="hidden" name="edicion" value="<c:out value='${edicionSeleccionada}'/>">
                                                            <input type="hidden" name="idInscripcion" value="${inscripcion.id}">
                                                            <button type="submit" name="estado" value="ACEPTADA" class="btn btn-sm btn-success">
                                                                <i class="bi bi-check-lg me-1"></i>
                                                                Aceptar
                                                            </button>
                                                            <button type="submit" name="estado" value="RECHAZADA" class="btn btn-sm btn-outline-danger">
                                                                <i class="bi bi-x-lg me-1"></i>
                                                                Rechazar
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info mb-0">
                                    No hay estudiantes inscriptos en esta edición.
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
