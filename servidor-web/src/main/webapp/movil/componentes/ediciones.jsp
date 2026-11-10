<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>edEXT Móvil - Ediciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .marco { border: 1px solid #dee2e6; border-radius: .375rem; padding: .75rem; }
    </style>
</head>
<body>
<%@ include file="navbar.jspf" %>
<main class="container py-3">
    <h1 class="h4 mb-3">Ver Ediciones</h1>

    <c:if test="${not empty mensajeError}">
        <div class="alert alert-danger"><c:out value="${mensajeError}"/></div>
    </c:if>

    <%-- Paso 1: instituto --%>
    <div class="dropdown mb-3">
        <button class="btn btn-primary dropdown-toggle" type="button"
                data-bs-toggle="dropdown" aria-expanded="false">
            <c:out value="${empty institutoElegido ? 'Instituto' : institutoElegido}"/>
        </button>
        <ul class="dropdown-menu">
            <c:forEach var="inst" items="${institutos}">
                <c:url var="urlInstituto" value="/movil/ediciones">
                    <c:param name="instituto" value="${inst}"/>
                </c:url>
                <li><a class="dropdown-item" href="${urlInstituto}"><c:out value="${inst}"/></a></li>
            </c:forEach>
        </ul>
    </div>

    <c:if test="${empty institutoElegido}">
        <p class="text-muted">Elegí un instituto para ver sus cursos.</p>
    </c:if>

    <%-- Paso 2: cursos del instituto --%>
    <c:if test="${not empty institutoElegido}">
        <h2 class="h6">Cursos</h2>
        <c:choose>
            <c:when test="${empty cursos}">
                <p class="text-muted small">Este instituto no tiene cursos.</p>
            </c:when>
            <c:otherwise>
                <div class="list-group mb-3">
                    <c:forEach var="c" items="${cursos}">
                        <c:url var="urlCurso" value="/movil/ediciones">
                            <c:param name="instituto" value="${institutoElegido}"/>
                            <c:param name="curso" value="${c.nombre}"/>
                        </c:url>
                        <a class="list-group-item list-group-item-action ${c.nombre == cursoElegido ? 'active' : ''}"
                           href="${urlCurso}"><c:out value="${c.nombre}"/></a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <%-- Paso 3: ediciones del curso elegido --%>
    <c:if test="${not empty cursoElegido and not empty curso}">
        <h2 class="h6">Ediciones de <c:out value="${curso.nombre}"/></h2>
        <c:choose>
            <c:when test="${empty curso.ediciones.edicion}">
                <p class="text-muted small">Este curso no tiene ediciones.</p>
            </c:when>
            <c:otherwise>
                <div class="list-group mb-3">
                    <c:forEach var="ed" items="${curso.ediciones.edicion}">
                        <c:url var="urlEdicion" value="/movil/ediciones">
                            <c:param name="instituto" value="${institutoElegido}"/>
                            <c:param name="curso" value="${cursoElegido}"/>
                            <c:param name="edicion" value="${ed.nombre}"/>
                        </c:url>
                        <a class="list-group-item list-group-item-action ${ed.nombre == edicionSeleccionada.nombre ? 'active' : ''}"
                           href="${urlEdicion}"><c:out value="${ed.nombre}"/></a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <%-- Paso 4: datos de la edición --%>
    <c:if test="${not empty edicionSeleccionada}">
        <div class="marco">
            <h2 class="h6"><c:out value="${edicionSeleccionada.nombre}"/></h2>
            <div class="border-bottom py-1 small">Curso: <c:out value="${edicionSeleccionada.nombreCurso}"/></div>
            <div class="border-bottom py-1 small">Inicio: <c:out value="${edicionSeleccionada.fechaInicio}"/></div>
            <div class="border-bottom py-1 small">Fin: <c:out value="${edicionSeleccionada.fechaFin}"/></div>
            <div class="border-bottom py-1 small">
                Fecha de publicación:
                <c:out value="${empty edicionSeleccionada.fechaPublicacion ? 'No especificada' : edicionSeleccionada.fechaPublicacion}"/>
            </div>
            <div class="border-bottom py-1 small">
                Cupo:
                <c:out value="${edicionSeleccionada.cupo > 0 ? edicionSeleccionada.cupo : 'Sin límite especificado'}"/>
            </div>
            <div class="py-1 small">
                Docentes:
                <c:choose>
                    <c:when test="${empty edicionSeleccionada.docentes.docente}">Ninguno</c:when>
                    <c:otherwise>
                        <c:forEach var="doc" items="${edicionSeleccionada.docentes.docente}" varStatus="st">
                            <c:if test="${st.index > 0}">, </c:if><c:out value="${doc}"/>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
