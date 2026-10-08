<%-- 
    Document   : principal
    Created on : Sep 27, 2026, 9:43:52 PM
    Author     : adolfo
--%>
<%--
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Plataforma de Educación Virtual</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
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

        <main class="container bg-light py-4">
            <%
                // 1. Obtener la vista si fue enviada desde un Servlet (request attribute)
                String contenido = (String) request.getAttribute("contenido");
                
                // 2. Si no viene de un Servlet, obtener el parámetro URL (request parameter)
                if (contenido == null || contenido.trim().isEmpty()) {
                    contenido = request.getParameter("contenido");
                }

                // 3. Valor por defecto si no hay nada especificado
                if (contenido == null || contenido.trim().isEmpty()) {
                    contenido = "home";
                }

                // Definir la ruta JSP resultante
                String paginaJsp = "/componentes/vistas/home.jsp";

                // Si viene la ruta completa enviada por el Servlet (ej: /componentes/vistas/consultaCurso.jsp)
                if (contenido.endsWith(".jsp")) {
                    paginaJsp = contenido;
                } else {
                    // Mapeo por palabras clave desde enlaces tipo ?contenido=cursos
                    switch (contenido) {
                        case "listarCursos":
                            paginaJsp = "/componentes/vistas/listarCursos.jsp";
                            break;
                        
                        case "altaCurso":
                            paginaJsp = "/componentes/vistas/altaCurso.jsp";
                            break;
                        case "cursos":
                            paginaJsp = "/componentes/vistas/consultaCurso.jsp";
                            break;
                        case "usuarios":
                            paginaJsp = "/componentes/vistas/consultaUsuario.jsp";
                            break;
                        case "inscripcion":
                            paginaJsp = "/componentes/vistas/inscripcionEdicion.jsp";
                            break;
                        default:
                            paginaJsp = "/componentes/vistas/home.jsp";
                            break;
                    }
                }
            %>

            <!-- Inclusión dinámica del contenido central -->
            <jsp:include page="<%= paginaJsp %>" />
        </main>
    </div>

    <!-- JavaScript de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>