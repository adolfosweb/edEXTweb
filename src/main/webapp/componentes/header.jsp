<%-- 
    Document   : header
    Created on : Sep 25, 2026, 5:34:32?PM
    Author     : adolfo
--%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark py-3">
    <div class="container-fluid px-4">
        <!-- Nombre de la plataforma -->
        <a class="navbar-brand fw-bold" href="#">
            <i class="bi bi-mortarboard-fill me-2"></i>
            edExt
        </a>
        <!-- Buscador -->
        <form class="d-flex flex-grow-1 mx-lg-4 my-3 my-lg-0"style="max-width: 450px;"role="search"action="#"method="get">
            <input class="form-control me-2"type="search"name="busqueda"
                   placeholder="Buscar cursos y programas..."aria-label="Buscar cursos y programas">
            <button class="btn btn-outline-light" type="submit">
                <i class="bi bi-search"></i>
            </button>
        </form>
        <!-- Usuario -->
        <div class="d-flex align-items-center gap-3">
            <a href="#" class="btn btn-outline-light">Iniciar sesión</a>
            <div class="vr border border-light border-1 opacity-100"></div>
            <a href="#" class="btn btn-outline-light">Registrarse</a>
            <img src="https://placehold.co/40x40"alt="Foto de perfil"class="avatar-sm">
        </div>
    </div>
</nav>
