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
        
        <div class="flex-grow-1 mx-lg-4 my-3 my-lg-0 position-relative"
             style="max-width: 450px;">

            <form class="d-flex" id="formBusquedaGeneral"
                  action="#" method="get" autocomplete="off">

                <input class="form-control me-2"
                       type="search"
                       id="busquedaGeneral"
                       name="busqueda"
                       placeholder="Buscar cursos, programas y usuarios..."
                       aria-label="Buscar cursos, programas y usuarios">

                <button class="btn btn-outline-light" type="submit">
                    <i class="bi bi-search"></i>
                </button>
            </form>

            <div id="resultadosBusqueda"
                 class="list-group position-absolute w-100 shadow"
                 style="top: 100%; left: 0; z-index: 1050; display: none;">
            </div>
        </div>

        <script>
        document.addEventListener("DOMContentLoaded", function () {
            const buscador = document.getElementById("busquedaGeneral");
            const formulario = document.getElementById("formBusquedaGeneral");
            const resultados = document.getElementById("resultadosBusqueda");
            const contexto = "<%= request.getContextPath() %>";

            let temporizador;

            formulario.addEventListener("submit", function (e) {
                e.preventDefault();

                if (buscador.value.trim() !== "") {
                    buscar(buscador.value.trim());
                }
            });

            buscador.addEventListener("input", function () {
                console.log("Estoy escribiendo en el buscador");
                clearTimeout(temporizador);

                const texto = buscador.value.trim();

                if (texto === "") {
                    resultados.style.display = "none";
                    resultados.innerHTML = "";
                    return;
                }

                temporizador = setTimeout(function () {
                    buscar(texto);
                }, 250);
            });

            function buscar(texto) {
                fetch(contexto + "/BuscarGeneralServlet?q="
                      + encodeURIComponent(texto))
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("Error en la búsqueda");
                        }
                        return response.json();
                    })
                    .then(data => {
                        resultados.innerHTML = "";

                        if (data.resultados.length === 0) {
                            const mensaje = document.createElement("div");
                            mensaje.className = "list-group-item text-secondary";
                            mensaje.textContent = "No se encontraron coincidencias.";
                            resultados.appendChild(mensaje);
                        }

                        data.resultados.forEach(function (item) {
                            const enlace = document.createElement("a");
                            enlace.className =
                                "list-group-item list-group-item-action";
                            enlace.href = item.url;

                            const titulo = document.createElement("strong");
                            titulo.textContent = item.nombre;

                            const detalle = document.createElement("div");
                            detalle.className = "small text-secondary";
                            detalle.textContent = item.tipo + " · " + item.detalle;

                            enlace.appendChild(titulo);
                            enlace.appendChild(detalle);
                            resultados.appendChild(enlace);
                        });

                        const separador = document.createElement("div");
                        separador.className = "list-group-item p-0";
                        resultados.appendChild(separador);

                        const mostrarTodos = document.createElement("a");
                        mostrarTodos.className =
                            "list-group-item list-group-item-action fw-bold text-center";
                        mostrarTodos.href = contexto + "/ConsultaUsuarioServlet";
                        mostrarTodos.textContent = "Mostrar todos los usuarios";

                        resultados.appendChild(mostrarTodos);
                        resultados.style.display = "block";
                    })
                    .catch(error => {
                        console.error(error);
                        resultados.innerHTML = "";

                        const mensaje = document.createElement("div");
                        mensaje.className = "list-group-item text-danger";
                        mensaje.textContent = "No se pudo realizar la búsqueda.";
                        resultados.appendChild(mensaje);

                        const mostrarTodos = document.createElement("a");
                        mostrarTodos.className =
                            "list-group-item list-group-item-action fw-bold text-center";
                        mostrarTodos.href = contexto + "/ConsultaUsuarioServlet";
                        mostrarTodos.textContent = "Mostrar todos los usuarios";

                        resultados.appendChild(mostrarTodos);
                        resultados.style.display = "block";
                    });
            }

            document.addEventListener("click", function (e) {
                if (!formulario.contains(e.target)
                        && !resultados.contains(e.target)) {
                    resultados.style.display = "none";
                }
            });
        });
        </script>

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

                <img src="https://placehold.co/40x40"
                     alt="Foto de perfil"
                     class="avatar-sm">

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
