<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%
    Usuario usuarioPerfil = (Usuario) request.getAttribute("usuarioPerfil");
    String fechaNacimiento = usuarioPerfil.getFecNac() == null
            ? "" : usuarioPerfil.getFecNac().toString();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Modificar perfil</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .avatar-sm { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="/componentes/header.jsp" />
    <div class="d-flex">
        <jsp:include page="/componentes/menulateral.jsp" />
        <main class="container-fluid p-4 p-lg-5">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-8 col-xl-6">
                    <h1 class="h2 mb-4">Modificar perfil</h1>

                    <% if (Boolean.TRUE.equals(request.getAttribute("actualizado"))) { %>
                    <div class="alert alert-success" role="status">Los datos del perfil se actualizaron.</div>
                    <% } %>
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger" role="alert"><%= escapeHtml((String) request.getAttribute("error")) %></div>
                    <% } %>

                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <form action="<%= request.getContextPath() %>/ModificarPerfil" method="post">
                                <div class="mb-3">
                                    <label for="nickname" class="form-label">Nickname</label>
                                    <input type="text" id="nickname" class="form-control"
                                           value="<%= escapeHtml(usuarioPerfil.getNickname()) %>" readonly>
                                </div>

                                <div class="mb-3">
                                    <label for="correoElectronico" class="form-label">Correo electrónico</label>
                                    <input type="email" id="correoElectronico" class="form-control"
                                           value="<%= escapeHtml(usuarioPerfil.getCorreoElectronico()) %>" readonly>
                                </div>

                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre</label>
                                    <input type="text" id="nombre" name="nombre" class="form-control"
                                           value="<%= escapeHtml(usuarioPerfil.getNombre()) %>" required>
                                </div>

                                <div class="mb-3">
                                    <label for="apellido" class="form-label">Apellido</label>
                                    <input type="text" id="apellido" name="apellido" class="form-control"
                                           value="<%= escapeHtml(usuarioPerfil.getApellido()) %>" required>
                                </div>

                                <div class="mb-4">
                                    <label for="fecNac" class="form-label">Fecha de nacimiento</label>
                                    <input type="date" id="fecNac" name="fecNac" class="form-control"
                                           value="<%= fechaNacimiento %>" max="<%= java.time.LocalDate.now() %>" required>
                                </div>

                                <hr>
                                <p class="text-secondary">Deja estos campos en blanco para conservar tu contraseña actual.</p>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Nueva contraseña (opcional)</label>
                                    <input type="password" id="password" name="password" class="form-control"
                                           autocomplete="new-password">
                                </div>
                                <div class="mb-4">
                                    <label for="confirmarPassword" class="form-label">Confirmar nueva contraseña</label>
                                    <input type="password" id="confirmarPassword" name="confirmarPassword"
                                           class="form-control" autocomplete="new-password">
                                </div>

                                <div class="d-flex justify-content-end gap-2">
                                    <a href="<%= request.getContextPath() %>/componentes/principal.jsp"
                                       class="btn btn-outline-secondary">Cancelar</a>
                                    <button type="submit" class="btn btn-primary">Guardar cambios</button>
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
<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
