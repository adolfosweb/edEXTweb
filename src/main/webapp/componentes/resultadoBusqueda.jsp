<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resultados de búsqueda - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .resultado-card {
            height: 100%;
            min-height: 230px;
            transition: transform 0.15s ease, box-shadow 0.15s ease;
        }
        .resultado-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .12) !important;
        }
        .resultado-descripcion {
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
            min-height: 4.5em;
        }
        .tipo-resultado {
            font-size: .8rem;
            font-weight: 600;
            text-transform: capitalize;
        }
        .resultado-imagen {
            height: 180px;
            overflow: hidden;
            background-color: #f1f3f5;
        }
        .resultado-imagen img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .placeholder-imagen {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            color: #6c757d;
        }
        .placeholder-imagen i {
            font-size: 2.5rem;
        }
        .placeholder-imagen span {
            font-size: 0.875rem;
        }
        .placeholder-curso {
            background-color: #eaf2ff;
            color: #0d6efd;
        }
        .placeholder-generico {
            background-color: #f1f3f5;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <jsp:include page="menulateral.jsp" />
        <main class="container-fluid py-4 px-4 bg-light">
            <h2 class="mb-4 text-dark">Resultados de la búsqueda</h2>
            <!-- Búsqueda y filtro por tipo -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/buscador" method="GET">
                        <div class="row g-3 align-items-end">
                            <div class="col-lg-5">
                                <label for="q" class="form-label fw-semibold">Buscar</label>
                                <div class="input-group">
                                    <input type="search"
                                           class="form-control"
                                           id="q"
                                           name="q"
                                           value="<c:out value='${busquedaActual}'/>"
                                           placeholder="Cursos, programas o usuarios...">
                                    <button class="btn btn-primary" type="submit">
                                        <i class="bi bi-search me-1"></i>
                                        Buscar
                                    </button>
                                </div>
                            </div>
                            <div class="col-lg-4">
                                <label for="filtro" class="form-label fw-semibold">Buscar en</label>
                                <select class="form-select" id="filtro" name="filtro">
                                    <option value="todos" ${filtroActual == 'todos' ? 'selected' : ''}>Todos</option>
                                    <option value="curso" ${filtroActual == 'curso' ? 'selected' : ''}>Cursos</option>
                                    <option value="programa" ${filtroActual == 'programa' ? 'selected' : ''}>Programas</option>
                                    <option value="usuario" ${filtroActual == 'usuario' ? 'selected' : ''}>Usuarios</option>
                                </select>
                            </div>
                            <div class="col-lg-3">
                                <button type="submit" class="btn btn-outline-primary w-100">Aplicar filtros</button>
                            </div>
                        </div>
                        <!-- Conservar el orden seleccionado al cambiar el filtro -->
                        <input type="hidden" name="orden" value="<c:out value='${ordenActual}'/>">
                    </form>
                </div>
            </div>
            <!-- Cantidad de resultados y orden -->
            <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
                <p class="mb-0 text-secondary">
                    <strong class="text-dark">${resultados.size()}</strong>
                    <c:choose>
                        <c:when test="${resultados.size() == 1}">
                            resultado encontrado
                        </c:when>
                        <c:otherwise>
                            resultados encontrados
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty busquedaActual}">
                        para «<c:out value="${busquedaActual}"/>»
                    </c:if>
                </p>

                <form action="${pageContext.request.contextPath}/buscador" method="GET"
                      class="d-flex align-items-center gap-2">
                    <input type="hidden" name="q" value="<c:out value='${busquedaActual}'/>">
                    <input type="hidden" name="filtro" value="<c:out value='${filtroActual}'/>">

                    <label for="orden" class="form-label mb-0 text-nowrap">Ordenar por:</label>

                    <select class="form-select form-select-sm" id="orden" name="orden" onchange="this.form.submit()">
                        <option value="nombre_asc" ${ordenActual == 'nombre_asc' ? 'selected' : ''}>Nombre (A-Z)</option>
                        <option value="fecha_desc" ${ordenActual == 'fecha_desc' ? 'selected' : ''}>Más recientes</option>
                    </select>
                </form>
            </div>
            <hr class="mb-4">
            <!-- Tarjetas de resultados -->
            <c:choose>
                <c:when test="${not empty resultados}">
                    <div class="row g-4">
                        <c:forEach var="r" items="${resultados}">
                            <div class="col-12 col-md-6 col-xl-4">                              
                                <article class="card resultado-card shadow-sm h-100">
                                    <%-- Imagen o placeholder según el tipo de resultado --%>
                                    <div class="resultado-imagen">
                                        <c:choose>
                                            <%-- Cursos: placeholder temporal --%>
                                            <c:when test="${r.tipo == 'curso'}">
                                                <div class="placeholder-imagen placeholder-curso">
                                                    <i class="bi bi-journal-bookmark"></i>
                                                    <span>Imagen del curso</span>
                                                </div>
                                            </c:when>
                                            <%-- Programas: mostrar su imagen si existe --%>
                                            <c:when test="${r.tipo == 'programa' and not empty r.imagen}">
                                                <img src="<c:out value='${r.imagen}'/>"
                                                     alt="Imagen de <c:out value='${r.nombre}'/>"
                                                     class="img-fluid"
                                                     loading="lazy">
                                            </c:when>
                                            <%-- Usuarios: avatar provisional --%>
                                            <c:when test="${r.tipo == 'usuario' and not empty r.imagen}">
                                                <img src="<c:out value='${r.imagen}'/>"
                                                     alt="Imagen de perfil de <c:out value='${r.nombre}'/>"
                                                     class="img-fluid"
                                                     loading="lazy">
                                            </c:when>
                                            <%-- Fallback para programas y usuarios sin imagen --%>
                                            <c:otherwise>
                                                <div class="placeholder-imagen placeholder-generico">
                                                    <i class="${r.tipo == 'usuario' ? 'bi bi-person-circle' : 'bi bi-image'}"></i>
                                                    <span>
                                                        ${r.tipo == 'usuario' ? 'Sin foto de perfil' : 'Sin imagen disponible'}
                                                    </span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <div class="mb-3">
                                            <c:choose>
                                                <c:when test="${r.tipo == 'curso'}">
                                                    <span class="badge text-bg-primary tipo-resultado">
                                                        <i class="bi bi-journal-bookmark me-1"></i>
                                                        Curso
                                                    </span>
                                                </c:when>
                                                <c:when test="${r.tipo == 'programa'}">
                                                    <span class="badge text-bg-success tipo-resultado">
                                                        <i class="bi bi-collection me-1"></i>
                                                        Programa
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge text-bg-secondary tipo-resultado">
                                                        <i class="bi bi-person me-1"></i>
                                                        Usuario
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <h3 class="h5 card-title"><c:out value="${r.nombre}"/></h3>
                                        <p class="card-text text-secondary resultado-descripcion">
                                            <c:choose>
                                                <c:when test="${not empty r.descripcion}">
                                                    <c:out value="${r.descripcion}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${r.tipo == 'usuario'}">
                                                            Perfil de usuario: <c:out value="${r.id}"/>.
                                                        </c:when>
                                                        <c:otherwise>
                                                            No hay una descripción disponible.
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                        <div class="mt-auto pt-3">
                                            <c:choose>
                                                <c:when test="${r.tipo == 'programa'}">
                                                    <a class="btn btn-outline-primary btn-sm"
                                                       href="${pageContext.request.contextPath}/consultarPrograma?programa=${r.id}">
                                                        Consultar programa
                                                        <i class="bi bi-arrow-right ms-1"></i>
                                                    </a>
                                                </c:when>
                                                <c:when test="${r.tipo == 'usuario'}">
                                                    <c:url var="urlPerfil" value="/PerfilUsuarioServlet">
                                                        <c:param name="nickname" value="${r.id}"/>
                                                    </c:url>
                                                    <a class="btn btn-outline-primary btn-sm" href="${urlPerfil}">
                                                        Ver perfil
                                                        <i class="bi bi-arrow-right ms-1"></i>
                                                    </a>
                                                </c:when>
                                                <c:when test="${r.tipo == 'curso'}">
                                                    <c:url var="urlCurso" value="/SvltConsultarCurso">
                                                        <c:param name="nombre" value="${r.id}"/>
                                                    </c:url>
                                                    <a class="btn btn-outline-primary btn-sm" href="${urlCurso}">
                                                        Consultar curso
                                                        <i class="bi bi-arrow-right ms-1"></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button"
                                                            class="btn btn-outline-secondary btn-sm" disabled>
                                                        Más información
                                                        <i class="bi bi-arrow-right ms-1"></i>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </article>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="card shadow-sm">
                        <div class="card-body text-center py-5">
                            <i class="bi bi-search display-4 text-secondary"></i>
                            <h3 class="h5 mt-3">No se encontraron resultados</h3>
                            <p class="text-secondary mb-0">
                                Intente con otras palabras o seleccione otro tipo
                                de contenido.
                            </p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>
    </div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
