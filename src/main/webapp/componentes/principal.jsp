<%-- 
    Document   : principal
    Created on : Sep 27, 2026, 9:43:52 PM
    Author     : adolfo
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Plataforma de Educación Virtual</title>
    <!-- Bootstrap 5 CSS // Actualizo a ultima version @adolfo -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Iconos de Bootstrap // Actualizo a ulitma version @adolfo -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .avatar-sm { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
        .avatar-lg { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; }
        .course-card-img { height: 180px; object-fit: cover; }
    </style>
</head>

<body>
    <!-- Encabezado reutilizable -->
    <jsp:include page="header.jsp" />
    <!-- Contenido principal -->
    <div class="d-flex">
        <!-- Menu lateral reutilizable -->
        <jsp:include page="menulateral.jsp" />
        <main class="container bg-light">
            <h1>Bienvenido a edExt</h1>
            <p class="text-dark">Pagina de prueba.</p>
        </main>
    </div>
    <!-- JavaScript de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>