<%-- 
    Document   : header
    Created on : Sep 25, 2026, 5:34:32?PM
    Author     : adolfo
--%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark py-3">
    <div class="container-fluid px-4">
        <!-- Nombre de la plataforma -->
        <a class="navbar-brand fw-bold"
           href="${pageContext.request.contextPath}/componentes/principal.jsp">
            <i class="bi bi-mortarboard-fill me-2"></i>
            edExt
        </a>

        <!-- Buscador -->
        <form class="d-flex flex-grow-1 mx-lg-4 my-3 my-lg-0" style="max-width: 450px;"
              role="search" action="${pageContext.request.contextPath}/buscador" method="get">
            <input class="form-control me-2" type="search" name="q"
                   placeholder="Buscar cursos, programas o usuarios..."
                   aria-label="Buscar cursos, programas o usuarios...">
            <!-- Valores iniciales de la búsqueda -->
            <input type="hidden" name="filtro" value="todos">
            <input type="hidden" name="orden" value="nombre_asc">
            <button class="btn btn-outline-light" type="submit" aria-label="Buscar">
                <i class="bi bi-search"></i>
            </button>
        </form>
        <!-- Usuario -->
        <%
            Logica.Usuario usuario = (Logica.Usuario) session.getAttribute("usuario");
        %>

        <div class="d-flex align-items-center gap-3">

            <% if (usuario != null) { %>

                <a href="<%= request.getContextPath() %>/PerfilUsuarioServlet?nickname=<%= java.net.URLEncoder.encode(usuario.getNickname(), "UTF-8") %>"
                    class="btn btn-outline-light btn-sm">
                     <%= usuario.getNickname() %>
                </a>

                <div class="vr border border-light border-1 opacity-100"></div>

                <a href="${pageContext.request.contextPath}/LogoutServlet"
                    class="btn btn-outline-light">
                     Cerrar sesión
                </a>

                
                    <%
                        String[] extensionesFoto = {".jpg", ".jpeg", ".png"};
                        String urlFoto = null;

                        String directorioImagenes = application.getRealPath("/imagenesUsuarios");

                        if (directorioImagenes != null && usuario != null) {
                            java.nio.file.Path carpetaImagenes =
                                java.nio.file.Paths.get(directorioImagenes);

                            for (String extension : extensionesFoto) {
                                java.nio.file.Path archivoFoto =
                                    carpetaImagenes.resolve(usuario.getNickname() + extension);

                                if (java.nio.file.Files.isRegularFile(archivoFoto)) {
                                    urlFoto = request.getContextPath()
                                        + "/imagenesUsuarios/"
                                        + java.net.URLEncoder.encode(
                                            usuario.getNickname(), "UTF-8")
                                        + extension;
                                    break;
                                }
                            }
                        }
                    %>

                    <% if (urlFoto != null) { %>
                        <img src="<%= urlFoto %>"
                             alt="Foto de perfil de <%= usuario.getNickname() %>"
                             class="avatar-sm rounded-circle">
                    <% } else { %>
                        <div class="avatar-sm d-flex align-items-center justify-content-center
                                    rounded-circle bg-secondary text-white"
                             title="Sin foto de perfil">
                            <i class="bi bi-person-fill"></i>
                        </div>
                    <% } %>


            <% } else { %>

                <a href="${pageContext.request.contextPath}/componentes/login.jsp"
                class="btn btn-outline-light">
                 Iniciar sesión
                </a>

                <div class="vr border border-light border-1 opacity-100"></div>

                <a href="${pageContext.request.contextPath}/RegistroServlet"
                   class="btn btn-outline-light">
                    Registrarse
                </a>

            <% } %>

        </div>
    </div>
</nav>
