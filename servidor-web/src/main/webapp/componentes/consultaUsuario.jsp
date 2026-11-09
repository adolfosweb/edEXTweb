<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Consulta de Usuario - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <jsp:include page="menulateral.jsp" />
        <main class="container py-5">
            <h1 class="mb-4">Consulta de Usuario</h1>
            <p class="text-secondary">
                Seleccioná un usuario para consultar su perfil.
            </p>
            <div class="card shadow-sm">
                <div class="card-body">
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger">
                            <c:out value="${mensajeError}" />
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${empty usuarios}">

                            <div class="alert alert-info mb-0">
                                No hay usuarios registrados.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="mb-3">
                                <label for="buscadorUsuarios" class="form-label">
                                    Buscar usuario
                                </label>
                                <input type="search"
                                       id="buscadorUsuarios"
                                       class="form-control"
                                       placeholder="Buscar por apellido, nombre o nickname...">
                            </div>
                            <div class="list-group" id="listaUsuarios">
                                <c:forEach var="usuario" items="${usuarios}">
                                    <a class="list-group-item list-group-item-action usuario-item"
                                       data-busqueda="${usuario.apellido} ${usuario.nombre} ${usuario.nickname}"
                                       href="${pageContext.request.contextPath}/PerfilUsuarioServlet?nickname=${usuario.nickname}">
                                        <strong>
                                            <c:out value="${usuario.apellido}" />
                                            <c:out value="${usuario.nombre}" />
                                        </strong>
                                        <div class="text-secondary small">
                                            Nickname:
                                            <c:out value="${usuario.nickname}" />
                                        </div>
                                    </a>
                                </c:forEach>
                                <div id="sinResultados"
                                     class="alert alert-info mt-3"
                                     style="display: none;">
                                    No se encontraron usuarios.
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>
    <script>
        const buscador = document.getElementById("buscadorUsuarios");

        if (buscador) {
            const elementos = document.querySelectorAll(".usuario-item");
            const sinResultados = document.getElementById("sinResultados");

            buscador.addEventListener("input", function () {
                const texto = this.value.trim().toLowerCase();
                let encontrados = 0;

                elementos.forEach(function (elemento) {
                    const coincide = elemento.dataset.busqueda
                        .toLowerCase()
                        .includes(texto);

                    elemento.style.display = coincide ? "" : "none";

                    if (coincide) {
                        encontrados++;
                    }
                });

                sinResultados.style.display =
                    encontrados === 0 ? "block" : "none";
            });
        }
    </script>
</body>
</html>