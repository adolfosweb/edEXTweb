<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Programa - edExt</title>
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
            <h2 class="mb-4 text-dark">Consultar programa de formación</h2>
            <!-- 1. Seleccionar programa -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/consultarPrograma" method="GET">
                        <label for="programa" class="form-label fw-semibold">Elegir programa</label>
                        
                        <select id="programa" name="programa" class="form-select" onchange="this.form.submit()">
                            <option value="">Seleccione un programa</option>
                            <c:forEach var="p" items="${programas}">
                                <option value="${p.key}"
                                    ${programaSeleccionado.nombre == p.key ? 'selected' : ''}>
                                    <c:out value="${p.key}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
            </div>
            <!-- 2. Datos del programa -->
            <c:choose>
                <c:when test="${not empty programaSeleccionado}">
                    <section class="card shadow-sm mb-4">
                        <div class="row g-0">
                            <div class="col-md-4">
                                <c:choose>
                                    <c:when test="${not empty programaSeleccionado.imagen}">
                                        <img src="${pageContext.request.contextPath}${programaSeleccionado.imagen}"
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
                            </div>
                            <div class="col-md-8">
                                <div class="card-body p-4">
                                    <h3 class="card-title mb-3"><c:out value="${programaSeleccionado.nombre}"/></h3>
                                    <p class="text-secondary"><c:out value="${programaSeleccionado.descripcion}"/></p>

                                    <div class="row">
                                        <div class="col-sm-6 mb-2">
                                            <span class="fw-semibold">
                                                <i class="bi bi-calendar-event me-2"></i>
                                                Fecha de inicio
                                            </span>
                                            <p class="mb-0"><c:out value="${programaSeleccionado.fechaInicio}"/></p>
                                        </div>

                                        <div class="col-sm-6 mb-2">
                                            <span class="fw-semibold">
                                                <i class="bi bi-calendar-check me-2"></i>
                                                Fecha de fin
                                            </span>
                                            <p class="mb-0"><c:out value="${programaSeleccionado.fechaFin}"/></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        No hay programas registrados para consultar.
                    </div>
                </c:otherwise>
            </c:choose>
            <!-- 3. Cursos del programa -->
            <section class="mb-4">
                <h3 class="h4 mb-3">
                    <i class="bi bi-journal-bookmark me-2"></i>
                    Cursos del programa
                </h3>
                <div class="cursos-scroll g-3">
                    <c:forEach var="c" items="${programaSeleccionado.cursos}">
                        <div class="col-md-6 col-xl-4">
                            <div class="card h-100 shadow-sm">
                                <img src="https://placehold.co/600x300/e2e8f0/334155?text=<c:out value="${c.nombre}"/>"
                                     class="curso-img card-img-top"
                                     alt="Imagen del curso <c:out value="${c.nombre}"/>">
                                <div class="card-body">
                                    <h4 class="h5 card-title"><c:out value="${c.nombre}"/></h4>
                                    <p class="card-text text-secondary">
                                        <c:out value ="${c.descripcion}"/>
                                    </p>
                                    <c:url var="urlCurso" value="/SvltConsultarCurso">
                                        <c:param name="nombre" value="${c.nombre}"/>
                                    </c:url>
                                    <a href="${urlCurso}" class="btn btn-outline-primary btn-sm">Consultar curso<i class="bi bi-arrow-right ms-1"></i></a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
            <!-- 4. Categorías del programa -->
            <section class="mb-4">
                <h3 class="h4 mb-3">
                    <i class="bi bi-tags me-2"></i>
                    Categorías
                </h3>
                
                <div class="card shadow-sm">
                    <div class="card-body d-flex flex-wrap gap-2">
                        <!-- Cambiar cursos por categorias cuando sean agregadas -->
                        <c:forEach var="cat" items="${programaSeleccionado.cursos}">
                            <span class="badge text-bg-primary p-2">
                                <c:out value="${cat}"/>
                            </span>
                        </c:forEach>
                    </div>
                </div>
            </section>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>