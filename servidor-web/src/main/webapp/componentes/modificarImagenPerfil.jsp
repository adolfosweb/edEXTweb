<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cambiar foto de perfil - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <main class="container py-5">
        <div class="card shadow-sm mx-auto" style="max-width: 550px;">
            <div class="card-body p-4">
                <h1 class="h4 mb-3">Cambiar foto de perfil</h1>
                <p class="text-secondary">
                    Seleccioná una nueva imagen para tu perfil.
                    Se admiten archivos JPG, JPEG y PNG de hasta 5 MB.
                </p>
                <c:if test="${not empty errorFoto}">
                    <div class="alert alert-danger">
                        No se pudo actualizar la foto.
                        Verificá el archivo e intentá nuevamente.
                    </div>
                </c:if>
                <form method="POST"
                      action="${pageContext.request.contextPath}/ModificarImagenPerfilServlet"
                      enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="foto" class="form-label">
                            Nueva foto
                        </label>
                        <input type="file"
                               class="form-control"
                               id="foto"
                               name="foto"
                               accept=".jpg,.jpeg,.png,image/jpeg,image/png"
                               required>
                    </div>
                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">
                            Guardar foto
                        </button>
                        <c:url var="urlCancelar" value="/PerfilUsuarioServlet">
                            <c:param name="nickname" value="${nickname}" />
                        </c:url>
                        <a href="${urlCancelar}"
                           class="btn btn-outline-secondary">
                            Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </main>
</body>
</html>