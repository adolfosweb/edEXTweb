<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Tendencias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
</head>
<body>
    <jsp:include page="/componentes/header.jsp" />
    <div class="d-flex">
        <jsp:include page="/componentes/menulateral.jsp" />
        <main class="container-fluid bg-light p-4">
            <h1 class="mb-4">Tendencias</h1>
            <p class="text-secondary">Cursos más visitados, de mayor a menor cantidad de visitas.</p>

            <c:choose>
                <c:when test="${not empty mensajeError}">
                    <div class="alert alert-danger"><c:out value="${mensajeError}"/></div>
                </c:when>
                <c:when test="${empty cursos}">
                    <p>Todavía no hay cursos visitados.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-striped align-middle bg-white">
                            <thead>
                                <tr>
                                    <th scope="col" style="width: 70px;">#</th>
                                    <th scope="col">Curso</th>
                                    <th scope="col" class="text-end">Visitas</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="curso" items="${cursos}" varStatus="estado">
                                    <c:url var="urlCurso" value="/SvltConsultarCurso">
                                        <c:param name="nombre" value="${curso.nombre}"/>
                                    </c:url>
                                    <tr>
                                        <td><c:out value="${estado.count}"/></td>
                                        <td><a href="${urlCurso}"><c:out value="${curso.nombre}"/></a></td>
                                        <td class="text-end"><c:out value="${curso.visitas}"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/componentes/principal.jsp" class="btn btn-secondary mt-3">Volver</a>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
