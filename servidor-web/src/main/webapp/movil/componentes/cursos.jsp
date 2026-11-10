<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>edEXT Móvil - Cursos</title>
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
    <h1 class="h4 mb-3">Ver Cursos</h1>

    <div class="dropdown mb-3">
        <button class="btn btn-primary dropdown-toggle" type="button"
                data-bs-toggle="dropdown" aria-expanded="false">
            <c:out value="${empty institutoElegido ? 'Instituto' : institutoElegido}"/>
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/movil/cursos">Todos</a></li>
            <c:forEach var="inst" items="${institutos}">
                <c:url var="filtro" value="/movil/cursos">
                    <c:param name="instituto" value="${inst}"/>
                </c:url>
                <li><a class="dropdown-item" href="${filtro}"><c:out value="${inst}"/></a></li>
            </c:forEach>
        </ul>
    </div>

    <c:choose>
        <c:when test="${not empty mensajeError}">
            <div class="alert alert-danger"><c:out value="${mensajeError}"/></div>
        </c:when>
        <c:when test="${empty cursos}">
            <p class="text-muted">No hay cursos disponibles.</p>
        </c:when>
        <c:otherwise>
            <div class="marco">
                <c:forEach var="curso" items="${cursos}">
                    <c:url var="detalle" value="/movil/curso">
                        <c:param name="nombre" value="${curso.nombre}"/>
                    </c:url>
                    <a href="${detalle}" class="d-block text-decoration-none text-dark mb-3">
                        <div class="img-vacia"><c:out value="${curso.nombre.substring(0, 1)}"/></div>
                        <div class="small pt-1"><c:out value="${curso.nombre}"/></div>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
