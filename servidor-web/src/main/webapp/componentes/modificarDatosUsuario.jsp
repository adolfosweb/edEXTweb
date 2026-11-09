<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modificar datos - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <main class="container py-4">
        <div class="card shadow-sm mx-auto" style="max-width: 650px;">
            <div class="card-body p-4">
                <h1 class="h3 mb-4">Modificar datos de usuario</h1>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <c:out value="${error}" />
                    </div>
                </c:if>
                <c:choose>
                    <c:when test="${not empty usuario}">
                        <form action="${pageContext.request.contextPath}/ModificarDatosUsuarioServlet"
                              method="post">
                            <div class="mb-3">
                                <label for="nickname" class="form-label">
                                    Nickname
                                </label>
                                <input type="text"
                                       class="form-control"
                                       id="nickname"
                                       value="<c:out value='${usuario.nickname}'/>"
                                       readonly>
                                <input type="hidden"
                                       name="nickname"
                                       value="<c:out value='${usuario.nickname}'/>">
                            </div>
                            <div class="mb-3">
                                <label for="nombre" class="form-label">
                                    Nombre
                                </label>
                                <input type="text"
                                       class="form-control"
                                       id="nombre"
                                       name="nombre"
                                       value="<c:out value='${usuario.nombre}'/>"
                                       maxlength="100"
                                       required>
                            </div>
                            <div class="mb-3">
                                <label for="apellido" class="form-label">
                                    Apellido
                                </label>
                                <input type="text"
                                       class="form-control"
                                       id="apellido"
                                       name="apellido"
                                       value="<c:out value='${usuario.apellido}'/>"
                                       maxlength="100"
                                       required>
                            </div>
                            <div class="mb-3">
                                <label for="correo" class="form-label">
                                    Correo electrónico
                                </label>
                                <input type="email"
                                       class="form-control"
                                       id="correo"
                                       name="correo"
                                       value="<c:out value='${usuario.correoElectronico}'/>"
                                       maxlength="150"
                                       required>
                            </div>
                            <div class="mb-4">
                                <label for="fechaNacimiento" class="form-label">
                                    Fecha de nacimiento
                                </label>   
                                <input type="date"
                                        class="form-control"
                                        id="fechaNacimiento"
                                        name="fechaNacimiento"
                                        value="${usuario.fecNac}"
                                        required>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary">
                                    Guardar cambios
                                </button>
                                <a class="btn btn-outline-secondary"
                                   href="${pageContext.request.contextPath}/PerfilUsuarioServlet?nickname=${usuario.nickname}">
                                    Cancelar
                                </a>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning">
                            No se encontraron los datos del usuario.
                        </div>
                        <a href="${pageContext.request.contextPath}/"
                           class="btn btn-secondary">
                            Volver
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
</body>
</html>