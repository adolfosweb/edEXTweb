<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Información del curso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .course-image-placeholder {
            min-height: 240px;
            background-color: #e9ecef;
            border: 1px solid #dee2e6;
        }

        .course-image {
            width: 100%;
            min-height: 240px;
            max-height: 360px;
            object-fit: cover;
        }
    </style>
</head>
<body>
    <jsp:include page="/componentes/header.jsp" />
    <div class="d-flex">
        <jsp:include page="/componentes/menulateral.jsp" />
        <main class="container-fluid bg-light p-4">
            <h1 class="mb-4">
                <c:out value="${curso.nombre}" />
            </h1>
            <div class="row g-4">
                <div class="col-12 col-lg-5">
                    <c:choose>
                        <c:when test="${not empty curso.imagen}">
                            <c:url var="urlImgCurso" value="/imagen">
                                <c:param name="ruta" value="${curso.imagen}"/>
                            </c:url>
                            <img class="course-image rounded"
                                 src="${urlImgCurso}"
                                 alt="Imagen del curso">
                        </c:when>
                        <c:otherwise>
                            <div
                                class="course-image-placeholder rounded d-flex align-items-center justify-content-center text-secondary"
                                role="img"
                                aria-label="El curso no tiene una imagen asociada">
                                <span>Sin imagen asociada</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-12 col-lg-7">
                    <dl class="row">
                        <dt class="col-sm-4">Descripción</dt>
                        <dd class="col-sm-8">
                            <c:out value="${curso.descripcion}" />
                        </dd>
                        <dt class="col-sm-4">Duración</dt>
                        <dd class="col-sm-8">
                            <c:out value="${curso.duracion}" />
                        </dd>
                        <dt class="col-sm-4">Cantidad de horas</dt>
                        <dd class="col-sm-8">
                            <c:out value="${curso.cantHoras}" />
                        </dd>
                        <dt class="col-sm-4">Créditos</dt>
                        <dd class="col-sm-8">
                            <c:out value="${curso.cantCreditos}" />
                        </dd>
                        <dt class="col-sm-4">Instituto</dt>
                        <dd class="col-sm-8">
                            <c:out value="${curso.instituto}" />
                        </dd>
                        <dt class="col-sm-4">Fecha de registro</dt>
                        <dd class="col-sm-8">
                            <c:choose>
                                <c:when test="${not empty curso.fechaRegistro}">
                                    <c:out value="${curso.fechaRegistro}" />
                                </c:when>
                                <c:otherwise>
                                    No especificada
                                </c:otherwise>
                            </c:choose>
                        </dd>
                        <dt class="col-sm-4">URL</dt>
                        <dd class="col-sm-8">
                            <c:choose>
                                <c:when test="${empty curso.url}">
                                    No especificada
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${curso.url}" />
                                </c:otherwise>
                            </c:choose>
                        </dd>
                        <dt class="col-sm-4">Cursos previos</dt>
                        <dd class="col-sm-8">
                            <c:choose>
                                <c:when test="${empty curso.previas.previa}">
                                    No tiene cursos previos.
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="previa" items="${curso.previas.previa}" varStatus="estado">
                                        <c:if test="${estado.index > 0}">, </c:if>
                                        <c:out value="${previa}" />
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                        <dt class="col-sm-4">Categorías</dt>
                        <dd class="col-sm-8">
                            <c:choose>
                                <c:when test="${empty curso.categorias.categoria}">
                                    Sin categorías asociadas.
                                </c:when>
                                <c:otherwise>
                                    <div class="d-flex flex-wrap gap-2">
                                        <c:forEach var="categoria" items="${curso.categorias.categoria}">
                                            <span class="badge text-bg-secondary">
                                                <c:out value="${categoria}" />
                                            </span>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                    </dl>
                </div>
            </div>
            <section class="mt-5">
                <h2>Ediciones del curso</h2>
                <c:choose>
                    <c:when test="${empty curso.ediciones.edicion}">
                        <p>Este curso no tiene ediciones asociadas.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="list-group">
                            <c:forEach var="edicion" items="${curso.ediciones.edicion}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>
                                        <strong>
                                            <c:out value="${edicion.nombre}" />
                                        </strong>
                                        — <c:out value="${edicion.fechaInicio}" />
                                        a <c:out value="${edicion.fechaFin}" />
                                    </span>
                                    <c:url var="urlEdicion" value="/SvltConsultarCurso">
                                        <c:param name="nombre" value="${curso.nombre}" />
                                        <c:param name="edicion" value="${edicion.nombre}" />
                                    </c:url>
                                    <a class="btn btn-outline-primary btn-sm" href="${urlEdicion}">
                                        Ver información
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </section>
            <section class="mt-5">
                <h2>Programas de formación asociados</h2>
                <c:choose>
                    <c:when test="${empty curso.programas.programa}">
                        <p>Este curso no está asociado a programas de formación.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="list-group">
                            <c:forEach var="programa" items="${curso.programas.programa}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>
                                        <strong>
                                            <c:out value="${programa.nombre}" />
                                        </strong>
                                    </span>
                                    <c:url var="urlPrograma" value="/consultarPrograma">
                                        <c:param name="programa" value="${programa.nombre}" />
                                    </c:url>
                                    <a class="btn btn-outline-primary btn-sm" href="${urlPrograma}">
                                        Ver información
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </section>
            <c:if test="${not empty edicionSeleccionada}">
                <section class="card mt-5">
                    <div class="card-header">
                        <h2 class="h5 mb-0">
                            Edición: <c:out value="${edicionSeleccionada.nombre}" />
                        </h2>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty edicionSeleccionada.imagen}">
                            <c:url var="urlImgEdicion" value="/imagen">
                                <c:param name="ruta" value="${edicionSeleccionada.imagen}"/>
                            </c:url>
                            <img src="${urlImgEdicion}" class="course-image rounded mb-3" alt="Imagen de la edición">
                        </c:if>
                        <dl class="row mb-0">
                            <dt class="col-sm-4">Curso</dt>
                            <dd class="col-sm-8">
                                <c:out value="${edicionSeleccionada.nombreCurso}" />
                            </dd>
                            <dt class="col-sm-4">Fecha de inicio</dt>
                            <dd class="col-sm-8">
                                <c:out value="${edicionSeleccionada.fechaInicio}" />
                            </dd>
                            <dt class="col-sm-4">Fecha de fin</dt>
                            <dd class="col-sm-8">
                                <c:out value="${edicionSeleccionada.fechaFin}" />
                            </dd>
                            <dt class="col-sm-4">Cupo</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${edicionSeleccionada.cupo > 0}">
                                        <c:out value="${edicionSeleccionada.cupo}" />
                                    </c:when>
                                    <c:otherwise>
                                        Sin límite especificado
                                    </c:otherwise>
                                </c:choose>
                            </dd>
                            <dt class="col-sm-4">Fecha de publicación</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${not empty edicionSeleccionada.fechaPublicacion}">
                                        <c:out value="${edicionSeleccionada.fechaPublicacion}" />
                                    </c:when>
                                    <c:otherwise>
                                        No especificada
                                    </c:otherwise>
                                </c:choose>
                            </dd>
                            <dt class="col-sm-4">Docentes participantes</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${empty edicionSeleccionada.docentes.docente}">
                                        Ninguno
                                    </c:when>
                                    <c:otherwise>
                                        <ul class="mb-0">
                                            <c:forEach var="docente"
                                                       items="${edicionSeleccionada.docentes.docente}">
                                                <li><c:out value="${docente}" /></li>
                                            </c:forEach>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>
                            </dd>
                            <dt class="col-sm-4">Inscriptos</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${empty edicionSeleccionada.inscriptos.inscripto}">
                                        Ninguno
                                    </c:when>
                                    <c:otherwise>
                                        <ul class="mb-0">
                                            <c:forEach var="inscripto"
                                                       items="${edicionSeleccionada.inscriptos.inscripto}">
                                                <li><c:out value="${inscripto}" /></li>
                                            </c:forEach>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>
                            </dd>
                        </dl>
                    </div>
                </section>
            </c:if>
            <c:if test="${not empty programaSeleccionado}">
                <section class="card mt-5">
                    <div class="card-header">
                        <h2 class="h5 mb-0">
                            Programa: <c:out value="${programaSeleccionado.nombre}" />
                        </h2>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty programaSeleccionado.imagen}">
                            <c:url var="urlImgProg" value="/imagen">
                                <c:param name="ruta" value="${programaSeleccionado.imagen}"/>
                            </c:url>
                            <img src="${urlImgProg}" class="course-image rounded mb-3" alt="Imagen del programa">
                        </c:if>
                        <dl class="row mb-0">
                            <dt class="col-sm-4">Descripción</dt>
                            <dd class="col-sm-8">
                                <c:out value="${programaSeleccionado.descripcion}" />
                            </dd>
                            <dt class="col-sm-4">Fecha de inicio</dt>
                            <dd class="col-sm-8">
                                <c:out value="${programaSeleccionado.fechaInicio}" />
                            </dd>
                            <dt class="col-sm-4">Fecha de fin</dt>
                            <dd class="col-sm-8">
                                <c:out value="${programaSeleccionado.fechaFin}" />
                            </dd>
                            <dt class="col-sm-4">Fecha de alta</dt>
                            <dd class="col-sm-8">
                                <c:out value="${programaSeleccionado.fechaAlta}" />
                            </dd>
                            <dt class="col-sm-4">Cursos asociados</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${empty programaSeleccionado.nombresCursos}">
                                        Ninguno
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="nombreCurso" items="${programaSeleccionado.nombresCursos}" varStatus="estado">
                                            <c:if test="${estado.index > 0}">, </c:if>
                                            <c:out value="${nombreCurso}" />
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </dd>
                            <dt class="col-sm-4">Estudiantes inscriptos</dt>
                            <dd class="col-sm-8">
                                <c:choose>
                                    <c:when test="${empty programaSeleccionado.nombresInscriptos}">
                                        Ninguno
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="inscripto" items="${programaSeleccionado.nombresInscriptos}" varStatus="estado">
                                            <c:if test="${estado.index > 0}">, </c:if>
                                            <c:out value="${inscripto}" />
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </dd>

                        </dl>
                    </div>
                </section>
            </c:if>
            <a href="${pageContext.request.contextPath}/SvltListarCursos"
               class="btn btn-secondary mt-4">
                Volver a cursos
            </a>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>