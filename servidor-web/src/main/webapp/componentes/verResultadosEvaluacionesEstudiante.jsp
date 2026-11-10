<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resultados de evaluaciones</title>
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
                        <i class="bi bi-journal-check me-2"></i>
                        Resultados de evaluaciones
                    </h2>
                    <p class="text-muted mb-4">
                        Consulta los resultados de tus evaluaciones en ediciones cerradas de cursos.
                    </p>
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger" role="alert">
                            <c:out value="${mensajeError}"/>
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${not empty inscripciones}">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Curso</th>
                                            <th>Edición</th>
                                            <th>Fecha de inscripción</th>
                                            <th>Estado</th>
                                            <th>Nota</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="inscripcion" items="${inscripciones}">
                                            <tr>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty inscripcion.nombreCurso}">
                                                            <c:out value="${inscripcion.nombreCurso}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            Curso no disponible
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty inscripcion.nombreEdicion}">
                                                            <c:out value="${inscripcion.nombreEdicion}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            Edición no disponible
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty inscripcion.fechaInscripcion}">
                                                            <c:out value="${inscripcion.fechaInscripcion}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            No disponible
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${inscripcion.estado == 'INSCRIPTO'}">
                                                            <span class="badge text-bg-primary">
                                                                INSCRIPTO
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${inscripcion.estado == 'ACEPTADA'}">
                                                            <span class="badge text-bg-success">
                                                                ACEPTADA
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${inscripcion.estado == 'RECHAZADA'}">
                                                            <span class="badge text-bg-danger">
                                                                RECHAZADA
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge text-bg-secondary">
                                                                <c:out value="${not empty inscripcion.estado ? inscripcion.estado : 'Sin estado'}"/>
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty inscripcion.nota}">
                                                            <strong>
                                                                <c:out value="${inscripcion.nota}"/>
                                                            </strong>
                                                            <span class="text-muted">/ 12</span>
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
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info mb-0">
                                Todavía no tienes resultados de evaluaciones disponibles en ediciones cerradas.
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <button type="button"
                            class="btn btn-outline-secondary mt-3"
                            onclick="history.back()">
                        <i class="bi bi-arrow-left me-1"></i>
                        Volver
                    </button>
                </div>
            </div>
        </main>
    </div>
</body>
</html>