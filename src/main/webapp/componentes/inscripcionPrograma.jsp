<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Inscripcion Programa - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"rel="stylesheet">
    <link rel="stylesheet"href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .programa-img {
            width: 100%;
            height: 220px;
            object-fit: cover;
        }
        .curso-img {
            width: 100%;
            height: 150px;
            object-fit: cover;
        }
        .cursos-scroll {
        display: flex;
        gap: 1rem;
        overflow-x: auto;
        padding-bottom: 10px;
        }
        .cursos-scroll > * {
            flex: 0 0 calc((100% - 2rem) / 3);
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <jsp:include page="menulateral.jsp" />
        <main class="container-fluid py-4 px-4 bg-light">
            <h2 class="mb-4 text-dark">Inscribirse a un programa de formación</h2>
            <!-- 1. Seleccionar programa -->
            <section class="mb-4">
                <h3 class="h4 mb-3">
                    <i class="bi bi-journal-bookmark me-2"></i>
                    Programas
                </h3>
                <div class="cursos-scroll g-3">
                    <c:forEach var="p" items="${programas}">
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <c:choose>
                                    <c:when test="${not empty p.value.imagen}">
                                        <img src="${pageContext.request.contextPath}${p.value.imagen}"
                                             class="card-img-top"
                                             alt="Portada del programa"
                                             style="height: 200px; object-fit: cover;">
                                    </c:when>

                                    <c:otherwise>
                                        <img src="https://placehold.co/600x300/e2e8f0/334155?text=Programa"
                                             class="card-img-top"
                                             alt="Programa sin imagen">
                                    </c:otherwise>
                                </c:choose>
                            <div class="card-body">
                                <h4 class="h5 card-title"><c:out value="${p.value.nombre}"/></h4>
                                <p class="card-text text-secondary"><c:out value="${p.value.descripcion}"/></p>
                                <a href="${pageContext.request.contextPath}/inscripcionPrograma?programa=${p.key}"
                                   class="btn btn-outline-primary btn-sm">Inscribirse<i class="bi bi-arrow-right ms-1"></i></a>
                                <a href="${pageContext.request.contextPath}/consultarPrograma?programa=${p.key}"
                                    class="btn btn-outline-primary btn-sm">Información<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>
                    </c:forEach>  
                </div>
            </section>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
