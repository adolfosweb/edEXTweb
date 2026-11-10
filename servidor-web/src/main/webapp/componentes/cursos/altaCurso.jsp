<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - ${modoEdicion ? 'Modificar curso' : 'Alta de curso'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .avatar-sm {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            object-fit: cover;
        }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="/componentes/header.jsp" />
    <div class="d-flex">
        <jsp:include page="/componentes/menulateral.jsp" />
        <main class="container-fluid p-4 p-lg-5">
            <div class="row justify-content-center">
                <div class="col-12 col-xl-9">
                    <h1 class="h2 mb-4">
                        <c:choose>
                            <c:when test="${modoEdicion}">
                                Modificar curso
                            </c:when>
                            <c:otherwise>
                                Alta de curso
                            </c:otherwise>
                        </c:choose>
                    </h1>
                    <c:if test="${mostrarExito}">
                        <div class="alert alert-success" role="status">
                            El curso fue dado de alta correctamente.
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            <c:out value="${error}" />
                        </div>
                    </c:if>
                    <c:if test="${cursoDuplicado}">
                        <div class="alert alert-warning">
                            <c:url var="urlModificar" value="/AltaCurso">
                                <c:param name="modo" value="editar" />
                                <c:param name="nombre" value="${cursoFormulario.nombre}" />
                            </c:url>
                            <a href="${urlModificar}">
                                Modificar los datos del curso existente
                            </a>
                            <span> o </span>
                            <a href="${pageContext.request.contextPath}/SvltListarCursos">
                                cancelar el alta
                            </a>.
                        </div>
                    </c:if>
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <form action="${pageContext.request.contextPath}/AltaCurso"
                                  method="post">
                                <c:if test="${modoEdicion}">
                                    <input type="hidden" name="modo" value="editar">
                                </c:if>
                                <div class="mb-3">
                                    <label for="nombre" class="form-label">
                                        Nombre del curso
                                    </label>
                                    <input
                                        type="text"
                                        id="nombre"
                                        name="nombre"
                                        class="form-control"
                                        value="<c:out value='${cursoFormulario.nombre}'/>"
                                        required
                                        <c:if test="${modoEdicion}">readonly</c:if>>
                                </div>
                                <div class="mb-3">
                                    <label for="descripcion" class="form-label">
                                        Descripción
                                    </label>
                                    <textarea
                                        id="descripcion"
                                        name="descripcion"
                                        class="form-control"
                                        rows="4"
                                        required><c:out value="${cursoFormulario.descripcion}"/></textarea>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="duracion" class="form-label">
                                            Duración
                                        </label>
                                        <input
                                            type="text"
                                            id="duracion"
                                            name="duracion"
                                            class="form-control"
                                            value="<c:out value='${cursoFormulario.duracion}'/>"
                                            required>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <label for="cantHoras" class="form-label">
                                            Cantidad de horas
                                        </label>
                                        <input
                                            type="number"
                                            id="cantHoras"
                                            name="cantHoras"
                                            min="0"
                                            class="form-control"
                                            value="${cursoFormulario.cantHoras}"
                                            required>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <label for="cantCreditos" class="form-label">
                                            Créditos
                                        </label>
                                        <input
                                            type="number"
                                            id="cantCreditos"
                                            name="cantCreditos"
                                            min="0"
                                            class="form-control"
                                            value="${cursoFormulario.cantCreditos}"
                                            required>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="url" class="form-label">
                                        URL asociada
                                    </label>
                                    <input
                                        type="url"
                                        id="url"
                                        name="url"
                                        class="form-control"
                                        value="<c:out value='${cursoFormulario.url}'/>"
                                        required>
                                </div>
                                <c:if test="${not modoEdicion}">
                                    <div class="mb-3">
                                        <label for="fechaRegistro" class="form-label">
                                            Fecha de registro
                                        </label>
                                        <input
                                            type="date"
                                            id="fechaRegistro"
                                            name="fechaRegistro"
                                            class="form-control"
                                            value="<c:out value='${cursoFormulario.fechaRegistro}'/>"
                                            min="${fechaMinima}"
                                            required>
                                    </div>
                                </c:if>
                                <div class="mb-3">
                                    <label for="instituto" class="form-label">
                                        Instituto que brindará el curso
                                    </label>
                                    <select
                                        id="instituto"
                                        name="instituto"
                                        class="form-select"
                                        required>
                                        <option value="">
                                            Selecciona un instituto
                                        </option>
                                        <c:forEach var="instituto"
                                                   items="${cursoFormulario.institutos.instituto}">
                                            <option
                                                value="<c:out value='${instituto}'/>"
                                                <c:if test="${instituto eq cursoFormulario.instituto}">
                                                    selected
                                                </c:if>>
                                                <c:out value="${instituto}" />
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${empty cursoFormulario.institutos.instituto}">
                                        <div class="form-text text-danger">
                                            Tu cuenta docente no tiene institutos asociados.
                                        </div>
                                    </c:if>
                                </div>
                                <div class="mb-4">
                                    <label class="form-label">
                                        Categorías del curso
                                    </label>
                                    <c:choose>
                                        <c:when test="${not empty cursoFormulario.categorias.categoria}">
                                            <div class="border rounded p-3">
                                                <c:forEach
                                                    var="categoria"
                                                    items="${cursoFormulario.categorias.categoria}"
                                                    varStatus="estado">
                                                    <div class="form-check">
                                                        <input
                                                            class="form-check-input"
                                                            type="checkbox"
                                                            name="categorias"
                                                            id="categoria_${estado.index}"
                                                            value="<c:out value='${categoria}'/>"
                                                            <c:if test="${cursoFormulario.categoriasSeleccionadas.categoriaSeleccionada.contains(categoria)}">
                                                                checked
                                                            </c:if>>
                                                        <label
                                                            class="form-check-label"
                                                            for="categoria_${estado.index}">
                                                            <c:out value="${categoria}" />
                                                        </label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <div class="form-text">
                                                Podés seleccionar una o varias categorías.
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-danger small">
                                                No hay categorías disponibles.
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="mb-4">
                                    <label class="form-label">
                                        Cursos previos
                                    </label>
                                    <c:choose>
                                        <c:when test="${not empty cursoFormulario.cursosExistentes.curso}">
                                            <div class="border rounded p-3">
                                                <c:forEach
                                                    var="nombreCurso"
                                                    items="${cursoFormulario.cursosExistentes.curso}"
                                                    varStatus="estado">
                                                    <div class="form-check">
                                                        <input
                                                            class="form-check-input"
                                                            type="checkbox"
                                                            name="previas"
                                                            id="previa_${estado.index}"
                                                            value="<c:out value='${nombreCurso}'/>"
                                                            <c:if test="${cursoFormulario.previasSeleccionadas.previa.contains(nombreCurso)}">
                                                                checked
                                                            </c:if>>
                                                        <label
                                                            class="form-check-label"
                                                            for="previa_${estado.index}">
                                                            <c:out value="${nombreCurso}" />
                                                        </label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <div class="form-text">
                                                Podés seleccionar ninguno, uno o varios cursos.
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="text-muted small">
                                                No hay otros cursos disponibles para seleccionar como previos.
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="d-flex justify-content-end gap-2">
                                    <a
                                        href="${pageContext.request.contextPath}/SvltListarCursos"
                                        class="btn btn-outline-secondary">
                                        Cancelar
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <c:choose>
                                            <c:when test="${modoEdicion}">
                                                Guardar cambios
                                            </c:when>
                                            <c:otherwise>
                                                Dar de alta curso
                                            </c:otherwise>
                                        </c:choose>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
