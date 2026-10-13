
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="Logica.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Consulta de Usuario - edExt</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body class="bg-light">

    <jsp:include page="header.jsp" />

    <main class="container py-5">

        <h1 class="mb-4">Consulta de Usuario</h1>

        <p class="text-secondary">
            Seleccioná un usuario para consultar su perfil.
        </p>

        <div class="card shadow-sm">
            <div class="card-body">

                <%
                    Map<String, Usuario> usuarios =
                        (Map<String, Usuario>) request.getAttribute("usuarios");
                %>

                <% if (usuarios == null || usuarios.isEmpty()) { %>

                    <div class="alert alert-info mb-0">
                        No hay usuarios registrados.
                    </div>

                <% } else { %>

                    <div class="list-group">

                        
<%
    java.util.List<Usuario> listaUsuarios =
            new ArrayList<>(usuarios.values());

    listaUsuarios.sort(
        Comparator.comparing(
            Usuario::getApellido,
            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
        ).thenComparing(
            Usuario::getNombre,
            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
        )
    );
%>

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

    <% for (Usuario usuario : listaUsuarios) { %>

        <a class="list-group-item list-group-item-action usuario-item"
           data-busqueda="<%= (usuario.getApellido() + " "
               + usuario.getNombre() + " "
               + usuario.getNickname()).toLowerCase() %>"
           href="<%= request.getContextPath() %>/PerfilUsuarioServlet?nickname=<%= java.net.URLEncoder.encode(usuario.getNickname(), "UTF-8") %>">

            <strong><%= usuario.getApellido() %>
                <%= usuario.getNombre() %></strong>

            <div class="text-secondary small">
                Nickname: <%= usuario.getNickname() %>
            </div>

        </a>

    <% } %>

    <div id="sinResultados" class="alert alert-info mt-3"
         style="display: none;">
        No se encontraron usuarios.
    </div>

</div>

<script>
    const buscador = document.getElementById("buscadorUsuarios");
    const elementos = document.querySelectorAll(".usuario-item");
    const sinResultados = document.getElementById("sinResultados");

    buscador.addEventListener("input", function () {
        const texto = this.value.trim().toLowerCase();
        let encontrados = 0;

        elementos.forEach(function (elemento) {
            const coincide = elemento.dataset.busqueda.includes(texto);

            elemento.style.display = coincide ? "" : "none";

            if (coincide) {
                encontrados++;
            }
        });

        sinResultados.style.display =
            encontrados === 0 ? "block" : "none";
    });
</script>


                <% } %>

            </div>
        </div>

    </main>

</body>
</html>
