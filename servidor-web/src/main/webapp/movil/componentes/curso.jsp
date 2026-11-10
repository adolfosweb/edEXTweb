<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>edEXT Móvil - <c:out value="${curso.nombre}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .marco { border: 1px solid #dee2e6; border-radius: .375rem; padding: .75rem; }
        .img-vacia {
            width: 100%; aspect-ratio: 16 / 9; background: #2a7f8e; color: #fff;
            font-size: 2.5rem; font-weight: 700; display: flex; align-items: center; justify-content: center;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jspf" %>
<main class="container py-3">
    <div class="marco">
        <h1 class="h5"><c:out value="${curso.nombre}"/></h1>
        <div class="small mb-2">Instituto de <c:out value="${curso.instituto}"/></div>

        <div class="img-vacia"><c:out value="${curso.nombre.substring(0, 1)}"/></div>

        <c:if test="${not empty curso.url}">
            <c:set var="enlace" value="${fn:startsWith(curso.url, 'http') ? curso.url : 'https://'.concat(curso.url)}"/>
            <a class="d-block small my-2" href="${enlace}" target="_blank" rel="noopener">Ir al sitio del curso</a>
        </c:if>

        <p class="small mt-2"><c:out value="${curso.descripcion}"/></p>

        <div class="border-bottom py-1 small">Duración: <c:out value="${curso.duracion}"/></div>
        <div class="border-bottom py-1 small">Horas: <c:out value="${curso.cantHoras}"/></div>
        <div class="border-bottom py-1 small">Créditos: <c:out value="${curso.cantCreditos}"/></div>
        <div class="border-bottom py-1 small">
            Fecha de registro:
            <c:out value="${empty curso.fechaRegistro ? 'No especificada' : curso.fechaRegistro}"/>
        </div>

        <c:if test="${not empty curso.categorias.categoria}">
            <div class="d-flex flex-wrap gap-1 mt-2">
                <c:forEach var="cat" items="${curso.categorias.categoria}">
                    <span class="badge text-bg-secondary"><c:out value="${cat}"/></span>
                </c:forEach>
            </div>
        </c:if>

        <h2 class="h6 mt-3">Ediciones del curso:</h2>
        <c:choose>
            <c:when test="${empty curso.ediciones.edicion}">
                <div class="small text-muted">Este curso no tiene ediciones.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="ed" items="${curso.ediciones.edicion}">
                    <c:url var="urlEdicion" value="/movil/curso">
                        <c:param name="nombre" value="${curso.nombre}"/>
                        <c:param name="edicion" value="${ed.nombre}"/>
                    </c:url>
                    <a class="d-block small" href="${urlEdicion}"><c:out value="${ed.nombre}"/></a>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        <h2 class="h6 mt-3">Programas de formación:</h2>
        <c:choose>
            <c:when test="${empty curso.programas.programa}">
                <div class="small text-muted">Sin programas asociados.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="prog" items="${curso.programas.programa}">
                    <div class="small"><c:out value="${prog.nombre}"/></div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty edicionSeleccionada}">
        <div class="marco mt-3">
            <h2 class="h6">Edición: <c:out value="${edicionSeleccionada.nombre}"/></h2>
            <div class="border-bottom py-1 small">Inicio: <c:out value="${edicionSeleccionada.fechaInicio}"/></div>
            <div class="border-bottom py-1 small">Fin: <c:out value="${edicionSeleccionada.fechaFin}"/></div>
            <div class="border-bottom py-1 small">
                Cupo:
                <c:out value="${edicionSeleccionada.cupo > 0 ? edicionSeleccionada.cupo : 'Sin límite especificado'}"/>
            </div>
            <div class="border-top py-1 small">
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

    <a href="${pageContext.request.contextPath}/movil/cursos" class="btn btn-secondary mt-3">Volver a cursos</a>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
