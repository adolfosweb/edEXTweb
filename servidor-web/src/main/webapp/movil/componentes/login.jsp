<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>edEXT Móvil - Iniciar sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { display: flex; align-items: center; min-height: 100vh; background: #f7f8fa; }
        .form-signin { width: 100%; max-width: 360px; padding: 1rem; margin: auto; }
        .logo-ext {
            width: 64px; height: 64px; border-radius: 50%; border: 3px solid #1d3557;
            color: #1d3557; font-weight: 700; margin: 0 auto 1rem;
            display: flex; align-items: center; justify-content: center;
        }
    </style>
</head>
<body>
<main class="form-signin">
    <form method="post" action="${pageContext.request.contextPath}/movil/login" id="formLogin" novalidate>
        <div class="logo-ext" aria-hidden="true">EXT</div>
        <h1 class="h4 mb-3 fw-normal text-center">Iniciar sesión</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2" role="alert"><c:out value="${error}"/></div>
        </c:if>

        <div class="form-floating">
            <input type="text" class="form-control rounded-bottom-0" id="identificador" name="identificador"
                   placeholder="Nickname o correo" autocomplete="username" required
                   value="<c:out value='${param.identificador}'/>">
            <label for="identificador">Nickname o correo electrónico</label>
        </div>
        <div class="form-floating">
            <input type="password" class="form-control rounded-top-0" id="password" name="password"
                   placeholder="Contraseña" autocomplete="current-password" required>
            <label for="password">Contraseña</label>
        </div>

        <div class="form-check text-start my-3">
            <input class="form-check-input" type="checkbox" value="true" id="recordarme" name="recordarme">
            <label class="form-check-label" for="recordarme">Recordarme</label>
        </div>

        <div id="avisoVacio" class="text-danger small mb-2 d-none">Completá el usuario y la contraseña.</div>
        <button class="btn btn-primary w-100 py-2" type="submit">Ingresar</button>
    </form>
</main>
<script>
    // Validación en el navegador: campos obligatorios.
    document.getElementById("formLogin").addEventListener("submit", function (e) {
        var id = document.getElementById("identificador").value.trim();
        var pw = document.getElementById("password").value;
        var vacio = id === "" || pw === "";
        document.getElementById("avisoVacio").classList.toggle("d-none", !vacio);
        if (vacio) { e.preventDefault(); }
    });
</script>
</body>
</html>
