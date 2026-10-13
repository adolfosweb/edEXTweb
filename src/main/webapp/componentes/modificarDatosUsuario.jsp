<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%@page import="java.time.format.DateTimeFormatter"%>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");

    String error = (String) request.getAttribute("error");

    String fechaNacimiento = "";

    if (usuario != null && usuario.getFecNac() != null) {
        fechaNacimiento = usuario.getFecNac().format(
                DateTimeFormatter.ISO_LOCAL_DATE
        );
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Modificar datos - edExt</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body class="bg-light">

    <jsp:include page="header.jsp" />

    <main class="container py-4">
        <div class="card shadow-sm mx-auto" style="max-width: 650px;">
            <div class="card-body p-4">

                <h1 class="h3 mb-4">Modificar datos de usuario</h1>

                <% if (error != null) { %>
                    <div class="alert alert-danger">
                        <%= error %>
                    </div>
                <% } %>

                <% if (usuario != null) { %>

                    <form action="<%= request.getContextPath() %>/ModificarDatosUsuarioServlet"
                          method="post">

                        <div class="mb-3">
                            <label for="nickname" class="form-label">
                                Nickname
                            </label>

                            <input type="text"
                                   class="form-control"
                                   id="nickname"
                                   value="<%= usuario.getNickname() %>"
                                   readonly>

                            <input type="hidden"
                                   name="nickname"
                                   value="<%= usuario.getNickname() %>">
                        </div>

                        <div class="mb-3">
                            <label for="nombre" class="form-label">
                                Nombre
                            </label>

                            <input type="text"
                                   class="form-control"
                                   id="nombre"
                                   name="nombre"
                                   value="<%= usuario.getNombre() != null ? usuario.getNombre() : "" %>"
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
                                   value="<%= usuario.getApellido() != null ? usuario.getApellido() : "" %>"
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
                                   value="<%= usuario.getCorreoElectronico() != null ? usuario.getCorreoElectronico() : "" %>"
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
                                   value="<%= fechaNacimiento %>"
                                   required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                Guardar cambios
                            </button>

                            <a class="btn btn-outline-secondary"
                               href="<%= request.getContextPath() %>/PerfilUsuarioServlet?nickname=<%= java.net.URLEncoder.encode(usuario.getNickname(), "UTF-8") %>">
                                Cancelar
                            </a>
                        </div>

                    </form>

                <% } else { %>

                    <div class="alert alert-warning">
                        No se encontraron los datos del usuario.
                    </div>

                    <a href="<%= request.getContextPath() %>/"
                       class="btn btn-secondary">
                        Volver
                    </a>

                <% } %>

            </div>
        </div>
    </main>

</body>
</html>